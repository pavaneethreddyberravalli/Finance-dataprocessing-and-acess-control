package com.finance.excption;

public class TokenRefreshException extends RuntimeException {
    public TokenRefreshException(String message) {
        super(message);
    }
}
