package edu.geekhub.homework.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import edu.geekhub.homework.users.interfaces.UserRepository;
import java.util.ArrayList;
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
    private User user;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository,
            userValidator);

        user = new User(1, "Mark", "Pearce", "Qwerty1", "some@gmail.com", Role.USER);
    }

    @Test
    void can_get_users() {
        List<User> expectedUsers = List.of(user);
        when(userRepository.getUsers()).thenReturn(expectedUsers);

        List<User> users = userService.getUsers();

        assertEquals(expectedUsers, users);
    }

    @Test
    void can_get_users_by_role() {
        List<User> expectedUsers = List.of(user);
        when(userRepository.getUsersByRole(any())).thenReturn(expectedUsers);

        List<User> users = userService.getUsersByRole(Role.USER);

        assertEquals(expectedUsers, users);
    }

    @Test
    void can_get_empty_users_by_null_role() {
        List<User> users = userService.getUsersByRole(null);

        assertEquals(new ArrayList<>(), users);
    }

    @Test
    void can_get_user_by_id() {
        doReturn(user).when(userRepository).getUserById(anyInt());

        User gotUser = userService.getUserById(1);

        assertEquals(user, gotUser);
    }

    @Test
    void can_get_null_user_by_wrong_id() {
        doReturn(null).when(userRepository).getUserById(anyInt());

        User gotUser = userService.getUserById(1);

        assertNull(gotUser);
    }

    @Test
    void can_get_user_by_email() {
        doReturn(user).when(userRepository).getUserByEmail(any());

        User gotUser = userService.getUserByEmail("some@gmail.com");

        assertEquals(user, gotUser);
    }

    @Test
    void can_add_user() {
        doNothing().when(userValidator).validate(any());
        doReturn(1).when(userRepository).addUser(any());
        userService = spy(this.userService);
        doReturn(user).when(userService).getUserById(anyInt());

        User addedUser = userService.addUser(user);

        assertNotNull(addedUser);
    }

    @Test
    void can_not_add_user_not_get_user_id_from_repository() {
        doNothing().when(userValidator).validate(any());
        doReturn(-1).when(userRepository).addUser(any());

        User addedUser = userService.addUser(user);

        assertNull(addedUser);
    }

    @Test
    void can_not_add_user_not_added_at_repository() {
        doNothing().when(userValidator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(userRepository).addUser(any());

        User addedUser = userService.addUser(user);

        assertNull(addedUser);
    }


    @Test
    void can_not_add_not_valid_user() {
        doThrow(new IllegalArgumentException()).when(userValidator).validate(any());

        User addedUser = userService.addUser(user);

        assertNull(addedUser);
    }

    @Test
    void can_delete_user_by_id() {
        userService = spy(this.userService);
        doReturn(user).when(userService).getUserById(anyInt());
        doNothing().when(userRepository).deleteUserById(anyInt());

        boolean successfulDeleted = userService.deleteUserById(1);

        assertTrue(successfulDeleted);
    }

    @Test
    void can_not_delete_user_by_not_existing_id() {
        userService = spy(this.userService);
        doReturn(null).when(userService).getUserById(anyInt());

        boolean successfulDeleted = userService.deleteUserById(-1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_not_delete_user_not_deleted_at_repository() {
        userService = spy(this.userService);
        doReturn(user).when(userService).getUserById(anyInt());
        doThrow(new DataAccessException("") {
        }).when(userRepository).deleteUserById(anyInt());

        boolean successfulDeleted = userService.deleteUserById(1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_update_user_by_id() {
        userService = spy(this.userService);
        doNothing().when(userValidator).validate(any());
        doReturn(user).when(userService).getUserById(anyInt());
        doNothing().when(userRepository).updateUserById(any(), anyInt());

        User updatedUser = userService.updateUserById(user, 1);

        assertNotNull(updatedUser);
    }

    @Test
    void can_not_update_user_by_id_to_not_valid_user() {
        doThrow(new IllegalArgumentException()).when(userValidator).validate(any());

        User updatedUser = userService.updateUserById(null, 1);

        assertNull(updatedUser);
    }

    @Test
    void can_not_update_user_by_not_existing_id() {
        userService = spy(this.userService);
        doNothing().when(userValidator).validate(any());
        doReturn(null).when(userService).getUserById(anyInt());

        User updatedUser = userService.updateUserById(user, 1);

        assertNull(updatedUser);
    }

    @Test
    void can_not_update_user_not_updated_at_repository() {
        userService = spy(this.userService);
        doNothing().when(userValidator).validate(any());
        doReturn(user).when(userService).getUserById(anyInt());
        doThrow(new DataAccessException("") {
        })
            .when(userRepository).updateUserById(any(), anyInt());

        User updatedUser = userService.updateUserById(user, 1);

        assertNull(updatedUser);
    }
}
