package edu.geekhub.homework.users;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {
    private UserValidator userValidator;
    private User user;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator();
        user = spy(
            new User(1, "Mark", "Pearce", "Qwerty1", "some@gmail.com", Role.USER)
        );
    }

    @Test
    void can_not_validate_null_user() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> userValidator.validate(null)
        );

        assertEquals("User was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_user_with_null_role() {
        doReturn(null).when(user).getRole();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> userValidator.validate(user)
        );

        assertEquals("User role was null", thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        ", User firstName was null",
        "'', User firstName was empty",
        "n, User firstName had wrong length",
    })
    void can_not_validate_user_with_wrong_name(String name, String expectedMessage) {
        doReturn(name).when(user).getFirstName();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> userValidator.validate(user)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        ", User lastName was null",
        "'', User lastName was empty",
        "l, User lastName had wrong length",
    })
    void can_not_validate_user_with_wrong_lastName(String lastName, String expectedMessage) {
        doReturn(lastName).when(user).getLastName();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> userValidator.validate(user)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        ", User password was null",
        "'', User password was empty",
        "123456, User password not match password pattern",
        "Pass1, User password had wrong length",
    })
    void can_not_validate_user_with_wrong_password(String password, String expectedMessage) {
        doReturn(password).when(user).getPassword();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> userValidator.validate(user)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        ", User email was null",
        "'', User email was empty",
        "mail.com, User email not match email pattern",
        "some@, User email had wrong length",
    })
    void can_not_validate_user_with_wrong_email(String email, String expectedMessage) {
        doReturn(email).when(user).getEmail();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> userValidator.validate(user)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_validate_correct_user() {
        assertDoesNotThrow(
            () -> userValidator.validate(user)
        );
    }

    @Test
    void can_validate_without_pass_correct_user() {
        user.setPassword("bcrypt password");

        assertDoesNotThrow(
            () -> userValidator.validateWithoutPass(user)
        );
    }
}
