package edu.geekhub.coursework;

import edu.geekhub.coursework.security.SecurityUser;
import edu.geekhub.coursework.usersparameters.UserParameters;
import edu.geekhub.coursework.usersparameters.interfaces.UserParametersService;
import edu.geekhub.coursework.util.TypeOfMeal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parameters")
public class UserParametersController {
    private final UserParametersService userParametersService;

    public UserParametersController(
        UserParametersService userParametersService
    ) {
        this.userParametersService = userParametersService;
    }

    @GetMapping
    public UserParameters getUserParametersById() {
        return userParametersService.getUserParametersByUserId(getUserId());
    }

    @PostMapping
    public UserParameters addUserParameters(
        @RequestBody UserParameters userParameters
    ) {
        userParameters.setUserId(getUserId());
        return userParametersService.addUserParameters(userParameters);
    }

    @PutMapping
    public UserParameters updateUserParameters(
        @RequestBody UserParameters userParameters
    ) {
        userParameters.setUserId(getUserId());
        return userParametersService.updateUserParameters(
            userParameters
        );
    }

    @GetMapping("/calories")
    public int getUserCaloriesForDay() {
        return userParametersService.getUserCaloriesForDay(getUserId());
    }

    @GetMapping("/calories/{typeOfMeal}")
    public int getUserCaloriesByTypeOfMeal(@PathVariable String typeOfMeal) {
        TypeOfMeal type = TypeOfMeal.valueOf(typeOfMeal);

        return userParametersService.getUserCaloriesByTypeOfMeal(
            getUserId(),
            type
        );
    }

    private int getUserId() {
        Object object = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        SecurityUser user = (SecurityUser) object;
        return user.getUserId();
    }
}
