package com.itmo.wtsc.controller;

import com.itmo.wtsc.ErrorMessages;
import com.itmo.wtsc.dto.NewCase;
import com.itmo.wtsc.dto.RequestDto;
import com.itmo.wtsc.services.RequestService;
import com.itmo.wtsc.services.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RequestController {

    @Getter
    private final RequestService requestService;

    @Getter
    private final UserService userService;

    @Autowired
    public RequestController(RequestService requestService, UserService userService) {
        this.requestService = requestService;
        this.userService = userService;
    }

    @RequestMapping(value = "/requests", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Transactional
    public RequestDto addRequest(@Validated(NewCase.class) @RequestBody RequestDto requestDto){
        return getRequestService().createRequest(requestDto, getUserService().getAuthenticatedUser());
    }

    @RequestMapping(value = "/requests", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Transactional
    public List<RequestDto> getRequests(){
        return getRequestService().getRequests();
    }

    @RequestMapping(value = "/requests/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Transactional
    public void deleteRequest(@PathVariable Integer id){
        getRequestService().deleteRequest(id);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleServiceNotFoundException(EmptyResultDataAccessException ex) {
    }
}
