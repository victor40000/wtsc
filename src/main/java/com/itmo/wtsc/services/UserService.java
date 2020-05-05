package com.itmo.wtsc.services;

import com.itmo.wtsc.entities.User;
import com.itmo.wtsc.repositories.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Getter
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUser() {
        String login = "user1";
        /*Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            login = ((UserDetails)principal).getUsername();
        } else {
            login = principal.toString();
        }*/
        return getUserRepository().getUserByLoginIs(login);
    }
}
