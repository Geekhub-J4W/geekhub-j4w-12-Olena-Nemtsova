package edu.geekhub.coursework.usersparameters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.coursework.TestApplication;
import edu.geekhub.coursework.config.DatabaseContainer;
import edu.geekhub.coursework.config.DatabaseTestConfig;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {DatabaseTestConfig.class, TestApplication.class})
@Testcontainers
class UserParametersRepositoryImplTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer =
        DatabaseContainer.getInstance();
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Mock
    private UserParametersValidator validator;
    private UserParametersRepositoryImpl userParametersRepository;

    @BeforeEach
    void setUp() {
        userParametersRepository = new UserParametersRepositoryImpl(validator, jdbcTemplate);
    }

    @Test
    void can_get_user_parameters_by_user_id() {
        UserParameters expectedUserParameters = new UserParameters(
            3,
            24,
            75,
            190,
            Gender.MALE,
            ActivityLevel.LOW,
            BodyType.ASTHENIC,
            Aim.NONE
        );

        UserParameters userParameters = userParametersRepository.getUserParametersByUserId(3);

        assertEquals(expectedUserParameters, userParameters);
    }

    @Test
    void can_get_null_user_parameters_by_wrong_user_id() {
        UserParameters userParameters = userParametersRepository.getUserParametersByUserId(-1);

        assertNull(userParameters);
    }

    @Test
    void can_add_user_parameters() {
        UserParameters expectedUserParameters = new UserParameters(
            1,
            40,
            80,
            190,
            Gender.MALE,
            ActivityLevel.LOW,
            BodyType.ASTHENIC,
            Aim.NONE
        );

        userParametersRepository.addUserParameters(expectedUserParameters);

        assertEquals(
            expectedUserParameters,
            userParametersRepository.getUserParametersByUserId(1)
        );
    }

    @Test
    void can_not_add_not_valid_user_parameters() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> userParametersRepository.addUserParameters(null)
        );
    }

    @Test
    void can_delete_user_parameters() {
        userParametersRepository.deleteUserParametersByUserId(3);

        assertNull(userParametersRepository.getUserParametersByUserId(3));
    }

    @Test
    void can_update_user_parameters() {
        UserParameters expectedUserParameters = new UserParameters(
            3,
            40,
            80,
            190,
            Gender.FEMALE,
            ActivityLevel.ZERO,
            BodyType.NORMOSTHENIC,
            Aim.LOSE
        );

        userParametersRepository.updateUserParametersByUserId(expectedUserParameters, 3);

        assertEquals(
            expectedUserParameters,
            userParametersRepository.getUserParametersByUserId(3)
        );
    }

    @Test
    void can_not_update_user_parameters_to_not_valid() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> userParametersRepository.updateUserParametersByUserId(null, 1)
        );
    }
}
