package edu.geekhub.coursework.usersparameters.interfaces;

import edu.geekhub.coursework.usersparameters.UserParameters;
import edu.geekhub.coursework.util.TypeOfMeal;

public interface UserParametersService {
    UserParameters getUserParametersByUserId(int userId);

    UserParameters addUserParameters(UserParameters userParameters);

    UserParameters updateUserParameters(UserParameters userParameters);

    int getUserCaloriesForDay(int userId);

    int getUserCaloriesByTypeOfMeal(int userId, TypeOfMeal typeOfMeal);
}
