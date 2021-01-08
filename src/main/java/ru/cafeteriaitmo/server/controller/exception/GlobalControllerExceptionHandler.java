package ru.cafeteriaitmo.server.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(NoEntityException.class)
    protected ResponseEntity<Object> handleException(NoEntityException e) {
        ResponseException response = new ResponseException(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyRequestBody.class)
    protected ResponseEntity<Object> handleException(EmptyRequestBody e) {
        ResponseException response = new ResponseException(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}