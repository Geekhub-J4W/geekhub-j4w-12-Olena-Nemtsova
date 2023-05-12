package edu.geekhub.coursework.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.coursework.TestApplication;
import edu.geekhub.coursework.config.DatabaseContainer;
import edu.geekhub.coursework.config.DatabaseTestConfig;
import java.util.ArrayList;
import java.util.List;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {DatabaseTestConfig.class, TestApplication.class})
@Testcontainers
class UserRepositoryImplTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer =
        DatabaseContainer.getInstance();
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Mock
    private UserValidator validator;
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl(validator, jdbcTemplate);
    }

    @Test
    void can_get_users() {
        List<User> users = userRepository.getUsers();

        Assertions.assertEquals(3, users.size());
    }

    @Test
    void can_get_user_by_id() {
        User expectedUser = new User(2, "Mark", "Pearce", "pass", "admin@gmail.com", Role.ADMIN);

        User user = userRepository.getUserById(2);

        assertEquals(expectedUser, user);
    }

    @Test
    void can_get_null_user_by_wrong_id() {
        User user = userRepository.getUserById(-1);

        assertNull(user);
    }

    @Test
    void can_get_user_by_email() {
        User expectedUser = new User(2, "Mark", "Pearce", "pass", "admin@gmail.com", Role.ADMIN);

        User user = userRepository.getUserByEmail("admin@gmail.com");

        assertEquals(expectedUser, user);
    }

    @Test
    void can_get_null_user_by_wrong_email() {
        User user = userRepository.getUserByEmail("some_not_existing@gmail.com");

        assertNull(user);
    }

    @Test
    void can_add_user() {
        User expectedUser = new User(-1, "Finn", "Human", "pass", "some@gmail.com", Role.USER);

        int newUserId = userRepository.addUser(expectedUser);

        expectedUser.setId(newUserId);
        assertEquals(expectedUser, userRepository.getUserById(newUserId));
    }

    @Test
    void can_not_add_product_with_not_unique_email() {
        User user = new User(-1, "Finn", "Human", "pass", "user@gmail.com", Role.USER);

        assertThrows(
            DuplicateKeyException.class,
            () -> userRepository.addUser(user)
        );
    }

    @Test
    void can_not_add_not_valid_user() {
        doThrow(new IllegalArgumentException()).when(validator).validateWithoutPass(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> userRepository.addUser(null)
        );
    }

    @Test
    void can_delete_user_by_id() {
        userRepository.deleteUserById(1);

        assertNull(userRepository.getUserById(1));
    }

    @Test
    void can_update_user_by_id() {
        User expectedUser = new User(1, "Finn", "Human", "pass", "some@gmail.com", Role.USER);

        userRepository.updateUserById(expectedUser, 1);

        assertEquals(expectedUser, userRepository.getUserById(1));
    }

    @Test
    void can_not_update_user_by_id_to_not_valid() {
        doThrow(new IllegalArgumentException()).when(validator).validateWithoutPass(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> userRepository.updateUserById(null, 1)
        );
    }

    @Test
    void can_update_user_without_password_by_id() {
        User expectedUser = new User(1, "Finn", "Human", "pass1", "some@gmail.com", Role.USER);

        userRepository.updateUserWithoutPasswordById(expectedUser, 1);

        expectedUser.setPassword("pass");
        assertEquals(expectedUser, userRepository.getUserById(1));
    }

    @Test
    void can_not_update_user_without_password_by_id_to_not_valid() {
        doThrow(new IllegalArgumentException()).when(validator).validateWithoutPass(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> userRepository.updateUserWithoutPasswordById(null, 1)
        );
    }

    @Test
    void can_get_users_by_role_page_and_input() {
        User user = new User(-1, "Mark", "Pearson", "pass", "some@gmail.com", Role.ADMIN);
        int newUserId = userRepository.addUser(user);
        user.setId(newUserId);
        List<User> expectedUsers = new ArrayList<>(List.of(user));

        List<User> users = userRepository
            .getUsersOfRoleByPageAndInput(Role.ADMIN, 1, 2, "mark pear");

        assertEquals(expectedUsers, users);
    }

    @Test
    void can_get_empty_users_by_wrong_input() {
        List<User> users = userRepository.getUsersOfRoleByPageAndInput(
            Role.USER,
            1,
            1,
            "some not existing user name"
        );

        assertEquals(new ArrayList<>(), users);
    }
}
