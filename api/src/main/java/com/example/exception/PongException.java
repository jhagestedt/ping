package com.example.exception;

public class PongException extends RuntimeException {

    public PongException(String reason) {
        super(reason);
    }
}
