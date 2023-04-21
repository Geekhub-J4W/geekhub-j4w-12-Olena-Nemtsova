package edu.geekhub.coursework.usersparameters;

import edu.geekhub.coursework.usersparameters.interfaces.UserParametersRepository;
import edu.geekhub.coursework.usersparameters.interfaces.UserParametersService;
import edu.geekhub.coursework.util.TypeOfMeal;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class UserParametersServiceImpl implements UserParametersService {
    private final UserParametersValidator validator;
    private final UserParametersRepository userParametersRepository;

    public UserParametersServiceImpl(UserParametersValidator validator,
                                     UserParametersRepository userParametersRepository) {
        this.validator = validator;
        this.userParametersRepository = userParametersRepository;
    }

    @Override
    public UserParameters getUserParametersByUserId(int userId) {
        return userParametersRepository.getUserParametersByUserId(userId);
    }

    @Override
    public UserParameters addUserParameters(UserParameters userParameters) {
        try {
            validator.validate(userParameters);
            if (getUserParametersByUserId(userParameters.getUserId()) != null) {
                throw new IllegalArgumentException("User parameters already exists");
            }
            userParametersRepository.addUserParameters(userParameters);

            userParameters = getUserParametersByUserId(userParameters.getUserId());
            Logger.info("UserParameters was added:\n" + userParameters);
            return userParameters;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn(
                "UserParameters wasn't added: " + userParameters + "\n" + exception.getMessage()
            );
            return null;
        }
    }

    @Override
    public UserParameters updateUserParameters(UserParameters userParameters) {
        try {
            int userId = userParameters.getUserId();
            if (getUserParametersByUserId(userId) == null) {
                throw new IllegalArgumentException(
                    "UserParameters with userId '" + userId + "' not found"
                );
            }
            validator.validate(userParameters);
            userParametersRepository.updateUserParametersByUserId(userParameters, userId);

            Logger.info("UserParameters was updated:\n" + userParameters);
            return getUserParametersByUserId(userId);
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("UserParameters wasn't updated to: "
                        + userParameters + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public int getUserCaloriesForDay(int userId) {
        double calories;
        UserParameters parameters = getUserParametersByUserId(userId);

        if (parameters == null) {
            Logger.warn("UserParameters with userId '" + userId + "' not found");
            return 0;
        }
        switch (parameters.getGender()) {
            case FEMALE -> calories = -9.26773
                                      + (0.296966 * parameters.getHeight())
                                      + (0.406986 * parameters.getWeight())
                                      - (0.15564 * parameters.getAge());
            case MALE -> calories = -10.8815
                                    + (0.319104 * parameters.getHeight())
                                    + (0.409487 * parameters.getWeight())
                                    - (0.14689 * parameters.getAge());
            default -> calories = 0;
        }
        calories = calories
                   * 24
                   * parameters.getActivityLevel().getCoefficient()
                   + parameters.getAim().getCaloriesDifference();

        return (int) Math.round(calories);
    }

    @Override
    public int getUserCaloriesByTypeOfMeal(int userId, TypeOfMeal typeOfMeal) {
        if (typeOfMeal == null) {
            Logger.warn("Type of meal was null");
            return 0;
        }

        double calories = getUserCaloriesForDay(userId);
        calories *= typeOfMeal.getPercentage();
        return (int) Math.round(calories);
    }
}
