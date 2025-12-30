package com.example.user_management.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("资源未找到");
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}