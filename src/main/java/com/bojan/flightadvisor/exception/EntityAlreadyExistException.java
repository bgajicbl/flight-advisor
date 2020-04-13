package com.bojan.flightadvisor.exception;

public class EntityAlreadyExistException extends RuntimeException {

    public EntityAlreadyExistException() {
        super();
    }

    public EntityAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EntityAlreadyExistException(final String message) {
        super(message);
    }

    public EntityAlreadyExistException(final Throwable cause) {
        super(cause);
    }
}
