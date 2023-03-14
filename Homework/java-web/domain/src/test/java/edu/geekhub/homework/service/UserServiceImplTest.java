package edu.geekhub.homework.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import edu.geekhub.homework.domain.User;
import edu.geekhub.homework.domain.UserValidator;
import edu.geekhub.homework.repository.interfaces.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository,
            userValidator);
    }

    @Test
    void can_get_users() {
        List<User> expectedUsers = List.of(
            new User("userId1", "name", "lName", "pass", "email", false)
        );
        when(userRepository.getUsers()).thenReturn(expectedUsers);

        List<User> users = userService.getUsers();

        assertEquals(expectedUsers, users);
    }

    @Test
    void can_get_only_admins() {
        User admin = new User("userId1", "name", "lName", "pass", "email", true);
        List<User> users = List.of(
            new User("userId1", "name", "lName", "pass", "email", false),
            admin
        );
        when(userRepository.getUsers()).thenReturn(users);

        List<User> admins = userService.getAdmins();

        assertEquals(List.of(admin), admins);
    }

    @Test
    void can_get_only_customers() {
        User customer = new User("userId1", "name", "lName", "pass", "email", false);
        List<User> users = List.of(
            new User("userId1", "name", "lName", "pass", "email", true),
            customer
        );
        when(userRepository.getUsers()).thenReturn(users);

        List<User> customers = userService.getCustomers();

        assertEquals(List.of(customer), customers);
    }

    @Test
    void can_get_user_by_id() {
        User expectedUser = new User("userId1", "name", "lName", "pass", "email", false);
        when(userRepository.getUserById(any())).thenReturn(expectedUser);

        User user = userService.getUserById("userId");

        assertEquals(expectedUser, user);
    }

    @Test
    void can_get_null_user_by_wrong_id() {
        when(userRepository.getUserById(any())).thenReturn(null);

        User user = userService.getUserById("");

        assertNull(user);
    }

    @Test
    void can_get_user_by_email_abd_password() {
        User expectedUser = new User("userId1", "name", "lName", "pass", "email", false);
        when(userRepository.getUsers()).thenReturn(List.of(expectedUser));

        User user = userService.getUserByEmailAndPassword("email", "pass");

        assertEquals(expectedUser, user);
    }

    @Test
    void can_get_null_user_by_not_exist_email_or_password() {
        User expectedUser = new User("userId1", "name", "lName", "pass", "email", false);
        when(userRepository.getUsers()).thenReturn(List.of(expectedUser));

        User user = userService.getUserByEmailAndPassword("wrongEmail", "pass");

        assertNull(user);
    }

    @Test
    void can_add_user() {
        doNothing().when(userValidator).validate(any());
        doReturn("userId1").when(userRepository).addUser(any());
        User someUser = new User();
        userService = spy(this.userService);
        doReturn(someUser).when(userService).getUserById(any());

        User addedUser = userService.addUser(someUser);

        assertNotNull(addedUser);
    }

    @Test
    void can_not_add_user_not_added_to_repository() {
        doNothing().when(userValidator).validate(any());
        doReturn(null).when(userRepository).addUser(any());

        User addedUser = userService.addUser(null);

        assertNull(addedUser);
    }

    @Test
    void can_not_add_not_valid_user() {
        doThrow(new IllegalArgumentException()).when(userValidator).validate(any());

        User addedUser = userService.addUser(null);

        assertNull(addedUser);
    }

    @Test
    void can_delete_user_by_id() {
        User someUser = new User("userId1", "name", "lName", "pass", "email", false);
        userService = spy(this.userService);
        doReturn(someUser).when(userService).getUserById(any());
        doNothing().when(userRepository).deleteUserById(any());

        boolean successfulDeleted = userService.deleteUserById("userId1");

        assertTrue(successfulDeleted);
    }

    @Test
    void can_not_delete_user_by_not_existing_id() {
        userService = spy(this.userService);
        doReturn(null).when(userService).getUserById(any());

        boolean successfulDeleted = userService.deleteUserById("");

        assertFalse(successfulDeleted);
    }

    @Test
    void can_not_delete_user_not_deleted_at_repository() {
        User someUser = new User("userId1", "name", "lName", "pass", "email", false);
        userService = spy(this.userService);
        doReturn(someUser).when(userService).getUserById(any());
        doThrow(new DataAccessException("") {
        }).when(userRepository).deleteUserById(any());

        boolean successfulDeleted = userService.deleteUserById("userId1");

        assertFalse(successfulDeleted);
    }

    @Test
    void can_update_user_by_id() {
        User someUser = new User("userId1", "name", "lName", "pass", "email", false);
        userService = spy(this.userService);
        doNothing().when(userValidator).validate(any());
        doReturn(someUser).when(userService).getUserById(any());
        doNothing().when(userRepository).updateUserById(any(), any());

        User updatedUser = userService.updateUserById(null, "");

        assertNotNull(updatedUser);
    }

    @Test
    void can_not_update_user_by_id_to_not_valid_product() {
        doThrow(new IllegalArgumentException()).when(userValidator).validate(any());

        User updatedUser = userService.updateUserById(null, "");

        assertNull(updatedUser);
    }

    @Test
    void can_not_update_user_by_not_existing_id() {
        userService = spy(this.userService);
        doNothing().when(userValidator).validate(any());
        doReturn(null).when(userService).getUserById(any());

        User updatedUser = userService.updateUserById(null, "");

        assertNull(updatedUser);
    }

    @Test
    void can_not_update_user_not_updated_at_repository() {
        userService = spy(this.userService);
        doNothing().when(userValidator).validate(any());
        User someUser = new User("userId1", "name", "lName", "pass", "email", false);
        doReturn(someUser).when(userService).getUserById(any());
        doThrow(new DataAccessException("") {
        })
            .when(userRepository).updateUserById(any(), any());

        User updatedUser = userService.updateUserById(null, "");

        assertNull(updatedUser);
    }
}
