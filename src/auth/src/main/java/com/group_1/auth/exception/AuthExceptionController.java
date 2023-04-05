package com.group_1.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

/**
 * exception
 * Created by NhatLinh - 19127652
 * Date 3/26/2023 - 1:41 PM
 * Description: ...
 */
@ControllerAdvice
public class AuthExceptionController {
    @ExceptionHandler(UserNotConfirmedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "USER_NOT_CONFIRMED")
    public void handleUserNotConfirmedException(
            UserNotConfirmedException exception
    ) {}

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "USER_NOT_FOUND")
    public void handleUserNotFoundException(
            UserNotFoundException exception
    ) {}

    @ExceptionHandler(NotAuthorizedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "USERNAME_PASSWORD_MISMATCH")
    public void handleUsernameAndPasswordMismatch(
            NotAuthorizedException exception
    ) {}
}
