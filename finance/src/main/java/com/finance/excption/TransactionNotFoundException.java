package com.finance.excption;

public class TransactionNotFoundException extends RuntimeException{
    public TransactionNotFoundException(String message) {
        super(message);
    }
}
