package com.itmo.wtsc.services;

import com.itmo.wtsc.dto.UserDto;
import com.itmo.wtsc.entities.ConfirmationToken;
import com.itmo.wtsc.entities.User;
import com.itmo.wtsc.repositories.TokenRepository;
import com.itmo.wtsc.repositories.UserRepository;
import com.itmo.wtsc.utils.ErrorMessages;
import com.itmo.wtsc.utils.converters.DtoConverter;
import com.itmo.wtsc.utils.enums.UserRole;
import com.itmo.wtsc.utils.exceptions.ValidationException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.itmo.wtsc.utils.ErrorMessages.*;
import static com.itmo.wtsc.utils.enums.UserRole.TOURIST;

@Service
public class UserService {

    @Getter
    private final PasswordEncoder encoder;

    @Getter
    private final UserRepository userRepository;

    @Getter
    private final EmailSenderService emailSenderService;

    @Getter
    private final TokenRepository tokenRepository;

    @Autowired
    public UserService(@Qualifier("wtsc.encoder") PasswordEncoder encoder,
                       UserRepository userRepository,
                       EmailSenderService emailSenderService,
                       TokenRepository tokenRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
        this.tokenRepository = tokenRepository;
    }

    public void registerUser(UserDto userDto) {
        if (getUserRepository().getUserByLoginIs(userDto.getLogin()) != null) {
            throw new ValidationException(String.format(USER_ALREADY_REGISTERED, userDto.getLogin()));
        }
        if (getUserRepository().getUserByEmailIs(userDto.getEmail()) != null) {
            throw new ValidationException(String.format(USER_ALREADY_REGISTERED, userDto.getEmail()));
        }
        if (UserRole.ADMIN.equals(userDto.getRole())) {
            throw new ValidationException(ADMIN_COULD_NOT_BE_REGISTERED);
        }
        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setPassword(getEncoder().encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        user.setActive(false);
        user.setEmail(userDto.getEmail());
        userRepository.save(user);
        verifyEmail(user);
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .filter(user -> !Objects.equals(user.getId(), getAuthenticatedUser().getId()))
                .map(DtoConverter::getUserDto)
                .collect(Collectors.toList());
    }

    public void changeUserStatus(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new ValidationException(String.format(USER_NOT_FOUND_ERROR, userDto.getId())));
        if (UserRole.ADMIN.equals(user.getRole())) {
            throw new ValidationException(String.format(USER_NOT_FOUND_ERROR, userDto.getId()));
        }
        if (!user.getToken().isActivated()) {
            throw new ValidationException(String.format(ACCOUNT_NOT_ACTIVATED, userDto.getId()));
        }
        user.setActive(userDto.isActive());
        userRepository.save(user);
    }

    public User getAuthenticatedUser() {
        String login;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            login = ((UserDetails)principal).getUsername();
        } else {
            login = principal.toString();
        }
        User user = getUserRepository().getUserByLoginIs(login);
        if (user == null) {
            throw new RuntimeException("Couldn't find authenticated user");
        }
        return user;
    }

    public boolean confirmUserEmail(String confirmationToken) {
        ConfirmationToken token = tokenRepository.findByConfirmationToken(confirmationToken);
        if (token != null && !token.isActivated())
        {
            User user = token.getUser();
            if (TOURIST.equals(user.getRole())) {
                user.setActive(true);
            }
            token.setActivated(true);
            userRepository.save(user);
            tokenRepository.save(token);
            return true;
        }
        return false;
    }

    private void verifyEmail(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationToken.setCreatedWhen(LocalDateTime.now());
        confirmationToken.setUser(user);
        confirmationToken.setActivated(false);

        getTokenRepository().save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("wtsc@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/confirm?token="+confirmationToken.getConfirmationToken());

        emailSenderService.sendEmail(mailMessage);
    }
}
