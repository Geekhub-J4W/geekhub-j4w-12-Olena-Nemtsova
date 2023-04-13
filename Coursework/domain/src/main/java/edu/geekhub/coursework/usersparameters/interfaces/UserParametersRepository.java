package edu.geekhub.coursework.usersparameters.interfaces;

import edu.geekhub.coursework.usersparameters.UserParameters;

public interface UserParametersRepository {
    void addUserParameters(UserParameters userParameters);

    UserParameters getUserParametersByUserId(int userId);

    void deleteUserParametersByUserId(int userId);

    void updateUserParametersByUserId(UserParameters userParameters, int userId);
}
