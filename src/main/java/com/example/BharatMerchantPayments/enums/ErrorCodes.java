package com.example.BharatMerchantPayments.enums;

public enum ErrorCodes {
    EXP001("User registration failed as user is already registered."),
    EXP002("User login failed.");

    private final String message;

    ErrorCodes(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }
}
