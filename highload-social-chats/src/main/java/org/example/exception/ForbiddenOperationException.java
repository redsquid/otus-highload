package org.example.exception;

public class ForbiddenOperationException extends Exception {

    public ForbiddenOperationException(String message) {
        super(message);
    }
}
