package com.group_1.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidCardException extends ResponseStatusException {
    public InvalidCardException(String cardNumber) {
        super(HttpStatus.NOT_ACCEPTABLE, String.format("%s is an invalid card", cardNumber));
    }
}

