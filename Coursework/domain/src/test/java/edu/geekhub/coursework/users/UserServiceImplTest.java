package edu.geekhub.coursework.users;

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

import edu.geekhub.coursework.users.interfaces.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private UserServiceImpl userService;
    @Mock
    private UserValidator validator;
    @Mock
    private UserRepository repository;
    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(validator, repository);

        user = new User(1, "Mark", "Pearce", "Qwerty1", "some@gmail.com", Role.USER);
    }

    @Test
    void can_get_user_by_id() {
        doReturn(user).when(repository).getUserById(anyInt());

        User gotUser = userService.getUserById(1);

        assertEquals(user, gotUser);
    }

    @Test
    void can_add_user() {
        doNothing().when(validator).validate(any());
        doReturn(null).when(repository).getUserByEmail(any());
        doReturn(1).when(repository).addUser(any());
        doReturn(user).when(repository).getUserById(anyInt());

        User addedUser = userService.addUser(user);

        assertNotNull(addedUser);
    }

    @Test
    void can_not_add_not_valid_user() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        User addedUser = userService.addUser(user);

        assertNull(addedUser);
    }

    @Test
    void can_not_add_user_with_role_super_admin() {
        user.setRole(Role.SUPER_ADMIN);
        doNothing().when(validator).validate(any());

        User addedUser = userService.addUser(user);

        assertNull(addedUser);
    }

    @Test
    void can_update_added_user_with_temp_password() {
        PasswordEncoder encoder = new BCryptPasswordEncoder(12);
        user.setPassword(encoder.encode("Temporary1"));

        doNothing().when(validator).validate(any());
        userService = spy(this.userService);
        doReturn(user).when(userService).getUserByEmail(any());
        doReturn(user).when(userService).updateUserById(any(), anyInt());

        User addedUser = userService.addUser(user);

        assertNotNull(addedUser);
    }

    @Test
    void can_not_add_user_not_added_at_repository() {
        doNothing().when(validator).validate(any());
        doReturn(null).when(repository).getUserByEmail(any());
        doThrow(new DataAccessException("") {
        }).when(repository).addUser(any());

        User addedUser = userService.addUser(user);

        assertNull(addedUser);
    }

    @Test
    void can_not_add_user_not_retrieved_generated_key() {
        doNothing().when(validator).validate(any());
        doReturn(null).when(repository).getUserByEmail(any());
        doReturn(-1).when(repository).addUser(any());

        User addedUser = userService.addUser(user);

        assertNull(addedUser);
    }

    @Test
    void can_delete_user_by_id() {
        doReturn(user).when(repository).getUserById(anyInt());
        doNothing().when(repository).deleteUserById(anyInt());

        boolean successfulDeleted = userService.deleteUserById(1);

        assertTrue(successfulDeleted);
    }

    @Test
    void can_not_delete_user_by_not_wrong_id() {
        doReturn(null).when(repository).getUserById(anyInt());

        boolean successfulDeleted = userService.deleteUserById(-1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_not_delete_user_with_role_super_admin() {
        user.setRole(Role.SUPER_ADMIN);
        doReturn(user).when(repository).getUserById(anyInt());

        boolean successfulDeleted = userService.deleteUserById(-1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_not_delete_user_not_deleted_at_repository() {
        doReturn(user).when(repository).getUserById(anyInt());
        doThrow(new DataAccessException("") {
        }).when(repository).deleteUserById(anyInt());

        boolean successfulDeleted = userService.deleteUserById(1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_update_user_by_id() {
        doNothing().when(validator).validate(any());
        doReturn(user).when(repository).getUserById(anyInt());
        doNothing().when(repository).updateUserById(any(), anyInt());

        User updatedUser = userService.updateUserById(user, 1);

        assertNotNull(updatedUser);
    }

    @Test
    void can_not_update_user_by_id_to_not_valid_user() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        User updatedUser = userService.updateUserById(null, 1);

        assertNull(updatedUser);
    }

    @Test
    void can_not_update_user_by_not_existing_id() {
        doNothing().when(validator).validate(any());
        doReturn(null).when(repository).getUserById(anyInt());

        User updatedUser = userService.updateUserById(user, 1);

        assertNull(updatedUser);
    }

    @Test
    void can_not_update_user_role_with_role_super_admin() {
        User userToUpdate = new User();
        userToUpdate.setRole(Role.SUPER_ADMIN);
        doNothing().when(validator).validate(any());
        doReturn(userToUpdate).when(repository).getUserById(anyInt());

        User updatedUser = userService.updateUserById(user, 1);

        assertNull(updatedUser);
    }

    @Test
    void can_not_update_user_role_to_role_super_admin() {
        User userToUpdate = new User();
        userToUpdate.setRole(Role.USER);
        user.setRole(Role.SUPER_ADMIN);
        doNothing().when(validator).validate(any());
        doReturn(userToUpdate).when(repository).getUserById(anyInt());

        User updatedUser = userService.updateUserById(user, 1);

        assertNull(updatedUser);
    }

    @Test
    void can_not_update_user_not_updated_at_repository() {
        doNothing().when(validator).validate(any());
        doReturn(user).when(repository).getUserById(anyInt());
        doThrow(new DataAccessException("") {
        })
            .when(repository).updateUserById(any(), anyInt());

        User updatedUser = userService.updateUserById(user, 1);

        assertNull(updatedUser);
    }

    @Test
    void can_get_users() {
        doReturn(List.of(user)).when(repository).getUsers();

        List<User> users = userService.getUsers();

        assertEquals(List.of(user), users);
    }

    @Test
    void can_get_users_by_role() {
        doReturn(List.of(user)).when(repository).getUsersByRole(any());

        List<User> users = userService.getUsersByRole(Role.USER);

        assertEquals(List.of(user), users);
    }

    @Test
    void can_get_empty_users_by_null_role() {
        List<User> users = userService.getUsersByRole(null);

        assertEquals(new ArrayList<>(), users);
    }
}
