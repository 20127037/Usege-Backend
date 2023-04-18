package com.group_1.master.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MasterExceptionController {
    @ExceptionHandler(NoSuchElementFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "NOT_FOUND")
    public void handleInvalidPasswordException(
            NoSuchElementFoundException exception
    ) {
    }
}
