package com.finance.excption;

public class ResourceAlreadyExitsException extends RuntimeException {
    public ResourceAlreadyExitsException(String message) {
        super(message);
    }
}

