package com.iron.kite_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class KiteNotFoundException extends RuntimeException {
    public KiteNotFoundException(String message) {
        super(message);
    }
}
