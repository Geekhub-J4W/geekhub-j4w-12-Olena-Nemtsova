package edu.geekhub.coursework.users;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {
    private UserValidator validator;
    private User user;

    @BeforeEach
    void setUp() {
        validator = new UserValidator();
        user = spy(
            new User(1, "Mark", "Pearce", "Qwerty1", "some@gmail.com", Role.USER)
        );
    }

    @Test
    void can_not_validate_null_user() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(null)
        );

        assertEquals("User was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_user_with_null_role() {
        doReturn(null).when(user).getRole();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(user)
        );

        assertEquals("User role was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_user_with_role_super_admin() {
        doReturn(Role.SUPER_ADMIN).when(user).getRole();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(user)
        );

        assertEquals("User has role 'SUPER_ADMIN'", thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        ", User firstName was null",
        "'', User firstName was empty",
        "n, User firstName had wrong length"
    })
    void can_not_validate_user_with_wrong_name(String name, String expectedMessage) {
        doReturn(name).when(user).getFirstName();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(user)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        ", User lastName was null",
        "'', User lastName was empty",
        "l, User lastName had wrong length"
    })
    void can_not_validate_user_with_wrong_lastName(String lastName, String expectedMessage) {
        doReturn(lastName).when(user).getLastName();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(user)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        ", User password was null",
        "'', User password was empty",
        "123456, User password not match password pattern",
        "Pass1, User password had wrong length"
    })
    void can_not_validate_user_with_wrong_password(String password, String expectedMessage) {
        doReturn(password).when(user).getPassword();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(user)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        ", User email was null",
        "'', User email was empty",
        "mail.com, User email not match email pattern",
        "some@, User email had wrong length"
    })
    void can_not_validate_user_with_wrong_email(String email, String expectedMessage) {
        doReturn(email).when(user).getEmail();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(user)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_validate_correct_user() {
        assertDoesNotThrow(
            () -> validator.validate(user)
        );
    }

    @Test
    void can_not_validate_users_for_update_with_null_user_to_update() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validateUsersForUpdate(null, user)
        );

        assertEquals("User was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_users_for_update_with_not_valid_new_user() {
        validator = spy(validator);
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> validator.validateUsersForUpdate(user, user)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "SUPER_ADMIN, USER, Can't update role of user with role 'SUPER_ADMIN'",
        "USER, SUPER_ADMIN, Can't update role of user to role 'SUPER_ADMIN'"
    })
    void can_not_validate_users_for_update_with_role_super_admin_not_same_at_each(
        String userToUpdateRole,
        String newUserRole,
        String expectedMessage
    ) {
        User userToUpdate = new User();
        userToUpdate.setRole(Role.valueOf(userToUpdateRole));
        user.setRole(Role.valueOf(newUserRole));

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validateUsersForUpdate(userToUpdate, user)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_validate_users_for_update_without_validate_new_password() {
        User userToUpdate = new User(user);
        user.setPassword(null);

        assertDoesNotThrow(
            () -> validator.validateUsersForUpdate(userToUpdate, user)
        );
    }

    @Test
    void can_validate_users_for_update() {
        assertDoesNotThrow(
            () -> validator.validateUsersForUpdate(user, user)
        );
    }

    @Test
    void can_validate_without_pass_correct_user() {
        user.setPassword("bcrypt password");

        assertDoesNotThrow(
            () -> validator.validateWithoutPass(user)
        );
    }

    @Test
    void can_not_validate_null_user_to_delete() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validateUserToDelete(null)
        );

        assertEquals("User was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_user_to_delete_with_role_super_admin() {
        user.setRole(Role.SUPER_ADMIN);

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validateUserToDelete(user)
        );

        assertEquals("User has role 'SUPER_ADMIN'", thrown.getMessage());
    }

    @Test
    void can_validate_user_to_delete() {
        assertDoesNotThrow(
            () -> validator.validateUserToDelete(user)
        );
    }
}
