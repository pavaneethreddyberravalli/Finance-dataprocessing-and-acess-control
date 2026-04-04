package com.finance.excption;

public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException(String message) {
        super(message);
    }
}
