package edu.geekhub.homework;

import edu.geekhub.models.User;


public class Validator {
    private static final UserDataFailedException[] EXCEPTIONS = new UserDataFailedException[]{
        new UserDataFailedException("Unsupported type of user"),
        new UserDataFailedException("Incorrect id"),
        new UserDataFailedException("Incorrect email"),
        new UserDataFailedException("Incorrect userName"),
        new UserDataFailedException("Incorrect fullName"),
        new UserDataFailedException("Incorrect age"),
        new UserDataFailedException("Incorrect notes"),
        new UserDataFailedException("Incorrect amountOfFollowers")
    };
    private static User user;
    private static UserService userService;

    private Validator() {
    }

    public static String validateUser(Object o, UserService service) {
        try {
            if (o == null || o.getClass() != User.class) {
                throw EXCEPTIONS[0];
            }
            user = (User) o;
            userService = service;

            return joinMessage(validateId()) +
                joinMessage(validateEmail()) +
                joinMessage(validateUserName()) +
                joinMessage(validateFullName()) +
                joinMessage(validateAge()) +
                joinMessage(validateNotes()) +
                joinMessage(validateAmountOfFollowers());
        } catch (UserDataFailedException e) {
            return e.getMessage();
        }
    }

    private static String joinMessage(String newMessage) {
        if (!newMessage.isBlank()) {
            return " " + newMessage;
        }
        return "";
    }

    private static String validateId() {
        try {
            if (user.getId() == null) {
                throw EXCEPTIONS[1];
            }
            if (userService.containId(user.getId())) {
                throw EXCEPTIONS[1];
            }
            return "";
        } catch (UserDataFailedException e) {
            return e.getMessage();
        }
    }

    private static String validateEmail() {
        try {
            String email = user.getEmail();
            if (email == null || email.isBlank()) {
                throw EXCEPTIONS[2];
            }
            if (userService.containEmail(email)) {
                throw EXCEPTIONS[2];
            }

            validateEmailChars(email);
            return "";
        } catch (UserDataFailedException e) {
            return e.getMessage();
        }
    }

    private static void validateEmailChars(String email) {
        char[] invalidSymbols = {'<', '>', '(', ')', '[', ']', ',', ';', '\\', '/', '"', ' ', '*', '\''};
        for (char symbol : invalidSymbols) {
            if (email.trim().contains(String.valueOf(symbol))) {
                throw EXCEPTIONS[2];
            }
        }
        if (!email.contains("@")) throw EXCEPTIONS[2];
    }

    private static String validateUserName() {
        try {
            String userName = user.getUserName();
            if (userName == null || userName.isBlank()) {
                throw EXCEPTIONS[3];
            }
            if (userService.containUserName(userName)) {
                throw EXCEPTIONS[3];
            }

            validateUserNameChars(userName);
            return "";
        } catch (UserDataFailedException e) {
            return e.getMessage();
        }
    }

    private static void validateUserNameChars(String userName) {
        char[] invalidSymbols = {'<', '>', '(', ')', '[', ']', ',', ';', '\\', '/', '"', '*', '\'', ' '};
        for (char symbol : invalidSymbols) {
            if (userName.trim().contains(String.valueOf(symbol))) {
                throw EXCEPTIONS[3];
            }
        }

        if (!userName.toLowerCase().equals(userName)) {
            throw EXCEPTIONS[3];
        }
    }

    private static String validateFullName() {
        try {
            String fullName = user.getFullName();
            if (fullName == null || fullName.isBlank()) {
                throw EXCEPTIONS[4];
            }

            validateFullNameWords(fullName);
            return "";
        } catch (UserDataFailedException e) {
            return e.getMessage();
        }
    }

    private static void validateFullNameWords(String fullName) {
        String[] words = fullName.trim().split("\\s+");

        if (words.length != 2) {
            throw EXCEPTIONS[4];
        }
        if (containsNotOnlyLetters(words[0]) && containsNotOnlyLetters(words[1])) {
            throw EXCEPTIONS[4];
        }

        validateCamelCase(words[0]);
        validateCamelCase(words[1]);
    }

    private static void validateCamelCase(String word) {
        if (Character.isLowerCase(word.charAt(0))) {
            throw EXCEPTIONS[4];
        }
        if (!word.substring(1).toLowerCase().equals(word.substring(1))) {
            throw EXCEPTIONS[4];
        }
    }

    public static boolean containsNotOnlyLetters(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (!Character.isLetter(word.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static String validateAge() {
        try {
            if (user.getAge() == null || user.getAge() < 18 || user.getAge() >= 100) {
                throw EXCEPTIONS[5];
            }
            return "";
        } catch (UserDataFailedException e) {
            return e.getMessage();
        }
    }

    private static String validateNotes() {
        try {
            if (user.getNotes() != null && !user.getNotes().isEmpty() && user.getNotes().length() >= 255) {
                throw EXCEPTIONS[6];
            }
            return "";
        } catch (UserDataFailedException e) {
            return e.getMessage();
        }
    }

    private static String validateAmountOfFollowers() {
        try {
            if (user.getAmountOfFollowers() == null || user.getAmountOfFollowers() < 0) {
                throw EXCEPTIONS[7];
            }
            return "";
        } catch (UserDataFailedException e) {
            return e.getMessage();
        }
    }
}
