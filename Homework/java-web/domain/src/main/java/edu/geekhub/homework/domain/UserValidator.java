package edu.geekhub.homework.domain;

public class UserValidator {

    public void validate(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User was null");
        }
        validateName(user.getFirstName(), "firstName");
        validateName(user.getLastName(), "lastName");
        validatePassword(user.getPassword());
        validateEmail(user.getEmail());
    }

    private void validateName(String name, String propertyName) {
        if (name == null) {
            throw new IllegalArgumentException("User " + propertyName + " was null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("User " + propertyName + " was empty");
        }
        if (name.length() > 50 || name.length() < 2) {
            throw new IllegalArgumentException("User " + propertyName + " had wrong length");
        }
    }

    private void validateEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("User email was null");
        }
        if (email.isBlank()) {
            throw new IllegalArgumentException("User email was empty");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("User email not match email pattern");
        }
        if (email.length() > 250 || email.length() < 9) {
            throw new IllegalArgumentException("User email had wrong length");
        }
    }

    private void validatePassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("User password was null");
        }
        if (password.isBlank()) {
            throw new IllegalArgumentException("User password was empty");
        }
        if (!validateSymbols(password)) {
            throw new IllegalArgumentException("User password not match email pattern");
        }
        if (password.length() > 20 || password.length() < 6) {
            throw new IllegalArgumentException("User password had wrong length");
        }
    }

    private boolean validateSymbols(String str) {
        int upperCase = 0;
        int lowerCase = 0;
        int digits = 0;
        for (char symbol : str.toCharArray()) {
            if (symbol >= 'A' && symbol <= 'Z') {
                upperCase++;
            } else if (symbol >= 'a' && symbol <= 'z') {
                lowerCase++;
            } else if (symbol >= '0' && symbol <= '9') {
                digits++;
            }
        }
        return upperCase != 0
               && lowerCase != 0
               && digits != 0;
    }
}