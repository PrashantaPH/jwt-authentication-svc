package com.authentication.exception;

public class InvalidAuthException extends RuntimeException{

    public InvalidAuthException(String message) {
        super(message);
    }

    public InvalidAuthException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
