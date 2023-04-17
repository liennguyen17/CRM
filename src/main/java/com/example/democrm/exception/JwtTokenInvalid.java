package com.example.democrm.exception;

public class JwtTokenInvalid extends RuntimeException {
    public JwtTokenInvalid(String message) {
        super(message);
    }
}
