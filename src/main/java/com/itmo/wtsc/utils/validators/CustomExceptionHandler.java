package com.itmo.wtsc.utils.validators;

import com.itmo.wtsc.utils.exceptions.DataNotFoundException;
import com.itmo.wtsc.utils.exceptions.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

import static com.itmo.wtsc.utils.ErrorMessages.INCORRECT_FIELD_ERROR;

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

        return new ResponseEntity<>(getBody(errors), headers, status);
    }

    @ExceptionHandler(DataNotFoundException.class)
    protected ResponseEntity<Object> handleDataNotFound(DataNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, getBody(Collections.singletonList(ex.getMessage())),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleValidationError(ValidationException ex, WebRequest request) {
        return handleExceptionInternal(ex, getBody(Collections.singletonList(ex.getMessage())),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    private Map<String, Object> getBody(List<String> errors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        if (errors.size() == 1) {
            body.put("error", errors.get(0));
        } else {
            body.put("errors", errors);
        }
        return body;
    }
}