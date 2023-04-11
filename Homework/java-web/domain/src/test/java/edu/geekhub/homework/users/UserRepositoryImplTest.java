package edu.geekhub.homework.users;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {
    private UserRepositoryImpl userRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;
    private User user;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl(jdbcTemplate, userValidator);
        user = new User(1, "Mark", "Pearce", "Qwerty1", "some@gmail.com", Role.USER);
    }

    @Test
    void can_not_add_not_valid_user() {
        doThrow(new IllegalArgumentException()).when(userValidator).validateWithoutPass(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> userRepository.addUser(null)
        );
    }

    @Test
    void can_not_add_not_added_at_database() {
        doNothing().when(userValidator).validateWithoutPass(any());
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), any(), any());

        assertThrows(
            DataAccessException.class,
            () -> userRepository.addUser(user)
        );
    }

    @Test
    void can_add_user() {
        doNothing().when(userValidator).validateWithoutPass(any());
        doReturn(1).when(jdbcTemplate).update(anyString(), any(), any());

        assertDoesNotThrow(
            () -> userRepository.addUser(user)
        );
    }

    @Test
    void can_not_update_to_valid_user() {
        doThrow(new IllegalArgumentException()).when(userValidator).validateWithoutPass(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> userRepository.updateUserById(null, 1)
        );
    }

    @Test
    void can_not_update_to_not_updated_at_database() {
        doNothing().when(userValidator).validateWithoutPass(any());
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertThrows(
            DataAccessException.class,
            () -> userRepository.updateUserById(user, 1)
        );
    }

    @Test
    void can_update_user() {
        doNothing().when(userValidator).validateWithoutPass(any());
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> userRepository.updateUserById(user, 1)
        );
    }

    @Test
    void can_not_delete_user_not_deleted_at_database() {
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertThrows(
            DataAccessException.class,
            () -> userRepository.deleteUserById(1)
        );
    }

    @Test
    void can_delete_user_by_id() {
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> userRepository.deleteUserById(1)
        );
    }

    @Test
    void can_get_users() {
        List<User> users = List.of(user);
        doReturn(users).when(jdbcTemplate).query(anyString(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> userRepository.getUsers()
        );
        assertEquals(users, userRepository.getUsers());
    }

    @Test
    void can_get_users_by_role() {
        doReturn(List.of(user))
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> userRepository.getUsersByRole(Role.USER)
        );
        assertEquals(List.of(user), userRepository.getUsersByRole(Role.USER));
    }

    @Test
    void can_get_user_by_id() {
        doReturn(List.of(user))
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> userRepository.getUserById(1)
        );
        assertEquals(user, userRepository.getUserById(1));
    }

    @Test
    void can_get_null_user_by_wrong_id() {
        doReturn(new ArrayList<>())
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> userRepository.getUserById(1)
        );
        assertNull(userRepository.getUserById(1));
    }

    @Test
    void can_get_user_by_email() {
        doReturn(List.of(user))
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> userRepository.getUserByEmail("some@gmail.com")
        );
        assertEquals(user, userRepository.getUserByEmail("some@gmail.com"));
    }

    @Test
    void can_get_null_user_by_wrong_email() {
        doReturn(new ArrayList<>())
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> userRepository.getUserByEmail("")
        );
        assertNull(userRepository.getUserByEmail(""));
    }
}
