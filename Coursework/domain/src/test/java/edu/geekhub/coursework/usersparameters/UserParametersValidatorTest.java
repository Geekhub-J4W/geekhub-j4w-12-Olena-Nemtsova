package edu.geekhub.coursework.usersparameters;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import edu.geekhub.coursework.users.User;
import edu.geekhub.coursework.users.interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserParametersValidatorTest {
    private UserParametersValidator validator;
    @Mock
    private UserRepository userRepository;
    private UserParameters userParameters;

    @BeforeEach
    void setUp() {
        validator = new UserParametersValidator(userRepository);

        userParameters = spy(
            new UserParameters(1, 24, 70, 189,
                Gender.MALE, ActivityLevel.LOW, BodyType.ASTHENIC, Aim.NONE)
        );
    }

    @Test
    void can_not_validate_null_userParameters() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(null)
        );

        assertEquals("UserParameters was null", thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        "15, UserParameters had invalid age",
        "150, UserParameters had invalid age"
    })
    void can_not_validate_userParameters_with_invalid_age(int age, String expectedMessage) {
        doReturn(age).when(userParameters).getAge();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(userParameters)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        "5, UserParameters had invalid weight",
        "500, UserParameters had invalid weight"
    })
    void can_not_validate_userParameters_with_invalid_weight(int weight, String expectedMessage) {
        doReturn(weight).when(userParameters).getWeight();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(userParameters)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        "5, UserParameters had invalid height",
        "500, UserParameters had invalid height"
    })
    void can_not_validate_userParameters_with_invalid_height(int height, String expectedMessage) {
        doReturn(height).when(userParameters).getHeight();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(userParameters)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_not_validate_userParameters_with_wrong_userId() {
        doReturn(null).when(userRepository).getUserById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(userParameters)
        );

        assertEquals("UserParameters had not exists user id", thrown.getMessage());
    }

    @Test
    void can_not_validate_userParameters_with_null_gender() {
        doReturn(null).when(userParameters).getGender();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(userParameters)
        );

        assertEquals("UserParameters gender was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_userParameters_with_null_activityLevel() {
        doReturn(null).when(userParameters).getActivityLevel();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(userParameters)
        );

        assertEquals("UserParameters activityLevel was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_userParameters_with_null_bodyType() {
        doReturn(null).when(userParameters).getBodyType();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(userParameters)
        );

        assertEquals("UserParameters bodyType was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_userParameters_with_null_aim() {
        doReturn(null).when(userParameters).getAim();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(userParameters)
        );

        assertEquals("UserParameters aim was null", thrown.getMessage());
    }

    @Test
    void can_validate_correct_userParameters() {
        doReturn(new User()).when(userRepository).getUserById(anyInt());

        assertDoesNotThrow(
            () -> validator.validate(userParameters)
        );
    }
}
