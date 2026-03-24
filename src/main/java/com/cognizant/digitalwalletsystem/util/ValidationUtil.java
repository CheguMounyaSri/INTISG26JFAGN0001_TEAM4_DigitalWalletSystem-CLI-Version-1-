package com.cognizant.digitalwalletsystem.util;

import com.cognizant.digitalwalletsystem.exception.InvalidInputException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_REGEX = "^[0-9]{10}$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!^&+=_])(?=\\S+$).{8,}$";

    public static void validateEmail(String email) throws InvalidInputException{
        if(email==null || !Pattern.matches(EMAIL_REGEX,email)){
            throw new InvalidInputException("Invalid email format.");
        }
    }

    public static void validatePhone(String phone) throws InvalidInputException{
        if(phone==null || !Pattern.matches(PHONE_REGEX,phone)){
            throw new InvalidInputException("Phone number must be exactly 10 digits.");
        }
    }

    public static void validatePassword(String password) throws InvalidInputException{
        if(password==null || !Pattern.matches(PASSWORD_REGEX,password)){
            throw new InvalidInputException("Password must be at least 8 characters long, contain an uppercase letter, lowercase letter,a digit, and a special character.");
        }
    }

    public static void validateAmount(java.math.BigDecimal amount) throws InvalidInputException{
        if(amount==null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidInputException("Amount must be greater than zero.");
        }
    }
}
