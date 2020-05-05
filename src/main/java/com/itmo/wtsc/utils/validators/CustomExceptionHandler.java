package com.itmo.wtsc.utils.validators;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

import static com.itmo.wtsc.ErrorMessages.INCORRECT_FIELD_ERROR;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format(INCORRECT_FIELD_ERROR, error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(getBody(errors, status), headers, status);
    }


/*    @ExceptionHandler(EmptyResultDataAccessException.class)
    protected ResponseEntity<Object> handleDataNotFound(EmptyResultDataAccessException ex, WebRequest request) {
        return handleExceptionInternal(ex, getBody(), HttpStatus.NOT_FOUND),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }*/

    private Map<String, Object> getBody(List<String> errors, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("errors", errors);
        return body;
    }
}