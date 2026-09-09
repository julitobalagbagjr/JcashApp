package jcash.util;

public class InputValidator {

    // Validate Philippine mobile number
    public static boolean isValidMobileNumber(String number) {
        return number.matches("09\\d{9}");
    }

    // Validate email address
    public static boolean isValidEmail(String email) {
        return email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        );
    }

    // Validate 4-digit PIN
    public static boolean isValidPIN(String pin) {
        return pin.matches("\\d{4}");
    }
}
