package com.example.exception;

public class PingException extends RuntimeException {

    public PingException(String reason) {
        super(reason);
    }
}
