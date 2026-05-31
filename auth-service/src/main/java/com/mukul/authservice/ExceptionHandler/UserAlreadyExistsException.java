package com.mukul.authservice.ExceptionHandler;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
