package com.group_1.account.exception;

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
public class UserAccountExceptionController {
    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "PASSWORD_INVALID")
    public void handleInvalidPasswordException(
            InvalidPasswordException exception
    ) {
    }

    @ExceptionHandler(CodeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "CODE_MISMATCH")
    public void handleCodeMismatchException(
            CodeMismatchException exception
    ) {
    }
    @ExceptionHandler(UserNotConfirmedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "USER_NOT_CONFIRMED")
    public void handleUserNotConfirmedException(
            UserNotConfirmedException exception
    ) {
    }

    @ExceptionHandler(ExpiredCodeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "CODE_EXPIRED")
    public void handleExpiredCodeException(
            ExpiredCodeException exception
    ) {
    }


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "USER_NOT_FOUND")
    public void handleUserNotFoundException(
            UserNotFoundException exception
    ) {
    }

    @ExceptionHandler(UsernameExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "USERNAME_EXISTS")
    public void handleUsernameExistsException(
            UsernameExistsException exception
    ) {
    }
}
