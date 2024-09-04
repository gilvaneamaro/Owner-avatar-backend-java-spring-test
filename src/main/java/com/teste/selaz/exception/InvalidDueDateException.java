package com.teste.selaz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDueDateException extends RuntimeException {
    public InvalidDueDateException(String message) {
        super(message);
    }

}
