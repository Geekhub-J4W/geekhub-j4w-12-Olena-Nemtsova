package edu.geekhub.coursework.users;

import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    public void validateWithoutPass(User user) {
        User userWithoutPass = new User(user);
        userWithoutPass.setPassword("Temporary1");
        validate(userWithoutPass);
    }

    public void validate(User user) {
        validateUserIsNotNull(user);
        validateName(user.getFirstName(), "firstName");
        validateName(user.getLastName(), "lastName");
        validatePassword(user.getPassword());
        validateEmail(user.getEmail());
        validateRole(user.getRole());
    }

    public void validateUserToDelete(User user) {
        validateUserIsNotNull(user);
        validateRole(user.getRole());
    }

    public void validateUsersForUpdate(User userToUpdate, User user) {
        validateUserIsNotNull(userToUpdate);

        User userWithoutRole = new User(user);
        userWithoutRole.setRole(Role.USER);

        if (user.getPassword() != null) {
            validate(userWithoutRole);
        } else {
            validateWithoutPass(userWithoutRole);
        }

        if (userToUpdate.getRole() == Role.SUPER_ADMIN
            && user.getRole() != Role.SUPER_ADMIN) {
            throw new IllegalArgumentException(
                "Can't update role of user with role 'SUPER_ADMIN'"
            );
        }

        if (user.getRole() == Role.SUPER_ADMIN
            && userToUpdate.getRole() != Role.SUPER_ADMIN) {
            throw new IllegalArgumentException(
                "Can't update role of user to role 'SUPER_ADMIN'"
            );
        }
    }

    private void validateUserIsNotNull(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User was null");
        }
    }

    private void validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("User role was null");
        }
        if (role == Role.SUPER_ADMIN) {
            throw new IllegalArgumentException("User has role 'SUPER_ADMIN'");
        }
    }

    private void validateName(String name, String propertyName) {
        if (name == null) {
            throw new IllegalArgumentException("User " + propertyName + " was null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("User " + propertyName + " was empty");
        }
        if (name.length() > 100 || name.length() < 2) {
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
            throw new IllegalArgumentException("User password not match password pattern");
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
