package edu.geekhub.coursework;

import edu.geekhub.coursework.security.SecurityUser;
import edu.geekhub.coursework.usersparameters.UserParameters;
import edu.geekhub.coursework.usersparameters.interfaces.UserParametersService;
import edu.geekhub.coursework.util.TypeOfMeal;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('USER')")
    public UserParameters getUserParametersById() {
        return userParametersService.getUserParametersByUserId(getUserId());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public UserParameters addUserParameters(
        @RequestBody UserParameters userParameters
    ) {
        userParameters.setUserId(getUserId());
        return userParametersService.addUserParameters(userParameters);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('USER')")
    public UserParameters updateUserParameters(
        @RequestBody UserParameters userParameters
    ) {
        return userParametersService.updateUserParametersByUserId(
            userParameters,
            getUserId()
        );
    }

    @GetMapping("/calories")
    @PreAuthorize("hasAuthority('USER')")
    public int getUserCaloriesForDay() {
        return userParametersService.getUserCaloriesForDay(getUserId());
    }

    @GetMapping("/calories/{typeOfMeal}")
    @PreAuthorize("hasAuthority('USER')")
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

        if (object.toString().equals("anonymousUser")) {
            return -1;
        }
        SecurityUser user = (SecurityUser) object;
        return user.getUserId();
    }
}
