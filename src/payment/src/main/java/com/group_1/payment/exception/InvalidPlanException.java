package com.group_1.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidPlanException extends ResponseStatusException {
    public InvalidPlanException(String plan) {
        super(HttpStatus.NOT_ACCEPTABLE, String.format("%s is an invalid plan", plan));
    }
}
