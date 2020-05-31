package com.itmo.wtsc.controller;

import com.itmo.wtsc.dto.UserDto;
import com.itmo.wtsc.dto.cases.NewUserCase;
import com.itmo.wtsc.dto.cases.UpdateUserCase;
import com.itmo.wtsc.repositories.UserRepository;
import com.itmo.wtsc.services.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Getter
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Transactional
    public void addUser(@RequestBody @Validated(NewUserCase.class) UserDto user) {
        userService.registerUser(user);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Transactional
    public void changeUser(@RequestBody @Validated(UpdateUserCase.class) UserDto user,
                           @PathVariable Integer id) {
        user.setId(id);
        userService.changeUserStatus(user);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Transactional
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }
}
