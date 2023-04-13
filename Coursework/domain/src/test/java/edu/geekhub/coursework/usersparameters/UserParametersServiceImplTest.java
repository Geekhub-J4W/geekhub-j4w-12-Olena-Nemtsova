package edu.geekhub.coursework.usersparameters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import edu.geekhub.coursework.usersparameters.interfaces.UserParametersRepository;
import edu.geekhub.coursework.util.TypeOfMeal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class UserParametersServiceImplTest {
    private UserParametersServiceImpl userParametersService;
    @Mock
    private UserParametersValidator validator;
    @Mock
    private UserParametersRepository repository;
    private UserParameters userParameters;

    @BeforeEach
    void setUp() {
        userParametersService = new UserParametersServiceImpl(validator, repository);
        userParameters = new UserParameters(1, 24, 70, 189,
            Gender.MALE, ActivityLevel.LOW, BodyType.ASTHENIC, Aim.NONE);
    }

    @Test
    void can_get_userParameters_by_userId() {
        doReturn(userParameters).when(repository).getUserParametersByUserId(anyInt());

        UserParameters gotUserParameters = userParametersService.getUserParametersByUserId(1);

        assertNotNull(gotUserParameters);
    }

    @Test
    void can_add_userParameters() {
        doNothing().when(validator).validate(any());
        doNothing().when(repository).addUserParameters(any());
        doReturn(userParameters).when(repository).getUserParametersByUserId(anyInt());

        UserParameters addedUserParameters = userParametersService
            .addUserParameters(userParameters);

        assertNotNull(addedUserParameters);
    }

    @Test
    void can_not_add_not_valid_userParameters() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        UserParameters addedUserParameters = userParametersService
            .addUserParameters(userParameters);

        assertNull(addedUserParameters);
    }

    @Test
    void can_not_add_userParameters_not_added_at_repository() {
        doNothing().when(validator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(repository).addUserParameters(any());

        UserParameters addedUserParameters = userParametersService
            .addUserParameters(userParameters);

        assertNull(addedUserParameters);
    }

    @Test
    void can_update_userParameters_by_userId() {
        userParametersService = spy(userParametersService);
        doReturn(userParameters).when(userParametersService).getUserParametersByUserId(anyInt());
        doNothing().when(validator).validate(any());
        doNothing().when(repository).updateUserParametersByUserId(any(), anyInt());

        UserParameters updatedUserParameters = userParametersService
            .updateUserParametersByUserId(userParameters, 1);

        assertNotNull(updatedUserParameters);
    }

    @Test
    void can_not_update_userParameters_by_wrong_userId() {
        userParametersService = spy(userParametersService);
        doReturn(null).when(userParametersService).getUserParametersByUserId(anyInt());

        UserParameters updatedUserParameters = userParametersService
            .updateUserParametersByUserId(userParameters, 1);

        assertNull(updatedUserParameters);
    }

    @Test
    void can_not_update_userParameters_by_userId_to_wrong_userParameters() {
        userParametersService = spy(userParametersService);
        doReturn(userParameters).when(userParametersService).getUserParametersByUserId(anyInt());
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        UserParameters updatedUserParameters = userParametersService
            .updateUserParametersByUserId(userParameters, 1);

        assertNull(updatedUserParameters);
    }

    @Test
    void can_not_update_userParameters_by_userId_not_updated_at_repository() {
        userParametersService = spy(userParametersService);
        doReturn(userParameters).when(userParametersService).getUserParametersByUserId(anyInt());
        doNothing().when(validator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(repository).updateUserParametersByUserId(any(), anyInt());

        UserParameters updatedUserParameters = userParametersService
            .updateUserParametersByUserId(userParameters, 1);

        assertNull(updatedUserParameters);
    }

    @Test
    void can_get_calories_for_day_by_userId_for_male() {
        userParametersService = spy(userParametersService);
        doReturn(userParameters).when(userParametersService).getUserParametersByUserId(anyInt());

        int caloriesForDay = userParametersService.getUserCaloriesForDay(1);

        assertEquals(2327, caloriesForDay);
    }

    @Test
    void can_get_calories_for_day_by_userId_for_female() {
        userParameters.setGender(Gender.FEMALE);
        userParametersService = spy(userParametersService);
        doReturn(userParameters).when(userParametersService).getUserParametersByUserId(anyInt());

        int caloriesForDay = userParametersService.getUserCaloriesForDay(1);

        assertEquals(2234, caloriesForDay);
    }

    @Test
    void can_get_zero_calories_for_day_by_wrong_userId() {
        userParametersService = spy(userParametersService);
        doReturn(null).when(userParametersService).getUserParametersByUserId(anyInt());

        int caloriesForDay = userParametersService.getUserCaloriesForDay(1);

        assertEquals(0, caloriesForDay);
    }

    @ParameterizedTest
    @CsvSource({
        "500, BREAKFAST",
        "700, DINNER",
        "300, LUNCH",
        "500, SUPPER"
    })
    void can_get_user_calories_by_type_of_meal(int expectedCalories, String typeOfMeal) {
        userParametersService = spy(userParametersService);
        doReturn(2000).when(userParametersService).getUserCaloriesForDay(anyInt());

        int caloriesByTypeOfMeal = userParametersService.getUserCaloriesByTypeOfMeal(
            1,
            TypeOfMeal.valueOf(typeOfMeal)
        );

        assertEquals(expectedCalories, caloriesByTypeOfMeal);
    }

    @Test
    void can_get_zero_calories_by_null_type_of_meal() {
        int caloriesByTypeOfMeal = userParametersService.getUserCaloriesByTypeOfMeal(1, null);

        assertEquals(0, caloriesByTypeOfMeal);
    }
}
