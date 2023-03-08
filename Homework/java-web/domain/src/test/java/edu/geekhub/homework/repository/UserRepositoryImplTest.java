package edu.geekhub.homework.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.homework.domain.User;
import edu.geekhub.homework.domain.UserValidator;
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

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl(jdbcTemplate, userValidator);
    }

    @Test
    void can_not_add_not_valid_user() {
        doThrow(new IllegalArgumentException()).when(userValidator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> userRepository.addUser(null)
        );
    }

    @Test
    void can_not_add_not_valid_user_at_database_lvl() {
        doNothing().when(userValidator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), any(), any());

        assertThrows(
            DataAccessException.class,
            () -> userRepository.addUser(new User())
        );
    }

    @Test
    void can_add_user() {
        doNothing().when(userValidator).validate(any());
        doReturn(1).when(jdbcTemplate).update(anyString(), any(), any());

        assertDoesNotThrow(
            () -> userRepository.addUser(new User())
        );
    }

    @Test
    void can_not_update_to_valid_user() {
        doThrow(new IllegalArgumentException()).when(userValidator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> userRepository.updateUserById(null, "")
        );
    }

    @Test
    void can_not_update_to_not_valid_product_at_database_lvl() {
        doNothing().when(userValidator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertThrows(
            DataAccessException.class,
            () -> userRepository.updateUserById(new User(), "")
        );
    }

    @Test
    void can_update_user() {
        doNothing().when(userValidator).validate(any());
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> userRepository.updateUserById(new User(), "")
        );
    }

    @Test
    void can_not_delete_user_not_exists_at_database_lvl() {
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertThrows(
            DataAccessException.class,
            () -> userRepository.deleteUserById("")
        );
    }

    @Test
    void can_delete_user_by_id() {
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> userRepository.deleteUserById("userId1")
        );
    }

    @Test
    void can_get_users_list() {
        List<User> users = List.of(new User());
        doReturn(users).when(jdbcTemplate).query(anyString(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> userRepository.getUsers()
        );
        assertEquals(users, userRepository.getUsers());
    }

    @Test
    void can_get_user_by_id() {
        doReturn(List.of(new User()))
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> userRepository.getUserById("userId1")
        );
        assertEquals(new User(), userRepository.getUserById("userId1"));
    }

    @Test
    void can_get_null_user_by_wrong_id() {
        doReturn(new ArrayList<>())
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> userRepository.getUserById("")
        );
        assertNull(userRepository.getUserById(""));
    }
}
