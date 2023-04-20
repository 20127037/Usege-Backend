package com.group_1.payment.exception;

import com.group_1.sharedDynamoDB.exception.NoSuchElementFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PaymentExceptionController {
    @ExceptionHandler(NoSuchElementFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "NOT_FOUND")
    public void handleInvalidNoElementException(
            NoSuchElementFoundException exception
    ) {
    }


    @ExceptionHandler(InvalidCardException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "INVALID_CARD")
    public void handleInvalidCardException(
            InvalidCardException exception
    ) {
    }

    @ExceptionHandler(InvalidPlanException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "INVALID_PLAN")
    public void handleInvalidCardException(
            InvalidPlanException exception
    ) {
    }
}
