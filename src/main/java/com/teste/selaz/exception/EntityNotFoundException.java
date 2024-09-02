package com.teste.selaz.exception;

public class EntityNotFoundException extends RuntimeException {
    private static final long servialVersionUID = 1L;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
