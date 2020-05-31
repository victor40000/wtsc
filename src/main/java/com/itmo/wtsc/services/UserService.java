package com.itmo.wtsc.services;

import com.itmo.wtsc.dto.UserDto;
import com.itmo.wtsc.entities.User;
import com.itmo.wtsc.repositories.UserRepository;
import com.itmo.wtsc.utils.ErrorMessages;
import com.itmo.wtsc.utils.enums.UserRole;
import com.itmo.wtsc.utils.exceptions.ValidationException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.itmo.wtsc.utils.ErrorMessages.USER_ALREADY_REGISTERED;
import static com.itmo.wtsc.utils.ErrorMessages.USER_NOT_FOUND_ERROR;
import static com.itmo.wtsc.utils.enums.UserRole.TOURIST;

@Service
public class UserService {

    @Getter
    private final PasswordEncoder encoder;

    @Getter
    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("wtsc.encoder") PasswordEncoder encoder, UserRepository userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    public void registerUser(UserDto userDto) {
        User user = getUserRepository().getUserByLoginIs(userDto.getLogin());
        if (user != null) {
            throw new ValidationException(String.format(USER_ALREADY_REGISTERED, userDto.getLogin()));
        }
        user = new User();
        user.setLogin(userDto.getLogin());
        user.setPassword(getEncoder().encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        user.setActive(TOURIST.equals(user.getRole()));
        userRepository.save(user);
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .filter(user -> !Objects.equals(user.getId(), getAuthenticatedUser().getId()))
                .map(this::getUserDto)
                .collect(Collectors.toList());
    }

    public void changeUserStatus(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new ValidationException(String.format(USER_NOT_FOUND_ERROR, userDto.getId())));
        if (Objects.equals(user.getId(), getAuthenticatedUser().getId())) {
            throw new ValidationException(String.format(USER_NOT_FOUND_ERROR, userDto.getId()));
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

    private UserDto getUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setLogin(user.getLogin());
        userDto.setId(user.getId());
        userDto.setRole(user.getRole());
        userDto.setActive(user.isActive());
        return userDto;
    }
}
