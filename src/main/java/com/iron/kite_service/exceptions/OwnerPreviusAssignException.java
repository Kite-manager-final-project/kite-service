package com.iron.kite_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OwnerPreviusAssignException extends RuntimeException {
    public OwnerPreviusAssignException(String message) {
        super(message);
    }
}
