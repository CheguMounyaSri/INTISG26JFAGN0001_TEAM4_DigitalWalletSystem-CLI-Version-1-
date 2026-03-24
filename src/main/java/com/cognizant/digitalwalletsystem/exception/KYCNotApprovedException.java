package com.cognizant.digitalwalletsystem.exception;

public class KYCNotApprovedException extends RuntimeException {
    public KYCNotApprovedException(String message) {
        super(message);
    }
}
