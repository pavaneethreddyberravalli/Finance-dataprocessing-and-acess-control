package com.finance.excption;

public class TokenNotFoundException extends RuntimeException{
    public TokenNotFoundException(String message) {
        super(message);
    }
}
