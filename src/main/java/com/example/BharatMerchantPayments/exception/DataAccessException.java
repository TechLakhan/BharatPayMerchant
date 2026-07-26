package com.example.BharatMerchantPayments.exception;

public class DataAccessException extends Exception{

    private final int statusCode;
    private final String cause;
    public DataAccessException(int statusCode, String cause) {
        this.statusCode = statusCode;
        this.cause = cause;
    }
    public DataAccessException(int statusCode, String message, String cause) {
        super(message);
        this.statusCode = statusCode;
        this.cause = cause;
    }
}
