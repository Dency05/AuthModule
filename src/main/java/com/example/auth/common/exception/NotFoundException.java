package com.example.auth.common.exception;

public class NotFoundException extends  RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
