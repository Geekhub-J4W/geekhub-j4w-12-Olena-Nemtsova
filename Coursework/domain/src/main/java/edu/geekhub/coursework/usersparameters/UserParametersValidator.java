package edu.geekhub.coursework.usersparameters;

import edu.geekhub.coursework.users.interfaces.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserParametersValidator {
    private final UserRepository userRepository;

    public UserParametersValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(UserParameters userParameters) {
        if (userParameters == null) {
            throw new IllegalArgumentException("UserParameters was null");
        }

        validateAge(userParameters.getAge());
        validateWeight(userParameters.getWeight());
        validateHeight(userParameters.getHeight());
        validateEnumProperty(userParameters.getActivityLevel(), "activityLevel");
        validateEnumProperty(userParameters.getAim(), "aim");
        validateEnumProperty(userParameters.getBodyType(), "bodyType");
        validateEnumProperty(userParameters.getGender(), "gender");
        validateUserId(userParameters.getUserId());
    }

    private void validateAge(int age) {
        if (age < 16 || age > 100) {
            throw new IllegalArgumentException("UserParameters had invalid age");
        }
    }

    private void validateWeight(int weight) {
        if (weight < 20 || weight > 250) {
            throw new IllegalArgumentException("UserParameters had invalid weight");
        }
    }

    private void validateHeight(int height) {
        if (height < 90 || height > 280) {
            throw new IllegalArgumentException("UserParameters had invalid height");
        }
    }

    private void validateUserId(int userId) {
        if (userRepository.getUserById(userId) == null) {
            throw new IllegalArgumentException("UserParameters had not exists user id");
        }
    }

    private <E extends Enum<E>> void validateEnumProperty(Enum<E> property, String propertyName) {
        if (property == null) {
            throw new IllegalArgumentException("UserParameters " + propertyName + " was null");
        }
    }
}
