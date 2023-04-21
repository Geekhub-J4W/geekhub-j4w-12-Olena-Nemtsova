package edu.geekhub.coursework.dishes;

import edu.geekhub.coursework.dishes.interfaces.DishRepository;
import edu.geekhub.coursework.dishes.interfaces.DishService;
import edu.geekhub.coursework.usersparameters.interfaces.UserParametersService;
import edu.geekhub.coursework.util.PageValidator;
import edu.geekhub.coursework.util.TypeOfMeal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class DishServiceImpl implements DishService {
    private final DishValidator validator;
    private final PageValidator pageValidator;
    private final DishRepository dishRepository;
    private final UserParametersService userParametersService;

    public DishServiceImpl(DishValidator validator,
                           PageValidator pageValidator,
                           DishRepository dishRepository,
                           UserParametersService userParametersService) {
        this.validator = validator;
        this.pageValidator = pageValidator;
        this.dishRepository = dishRepository;
        this.userParametersService = userParametersService;
    }

    @Override
    public Dish getDishById(int id) {
        return dishRepository.getDishById(id);
    }

    @Override
    public Dish addDish(Dish dish) {
        try {
            validator.validate(dish);
            int id = dishRepository.addDish(dish);
            if (id == -1) {
                throw new IllegalArgumentException("Unable to retrieve the generated key");
            }
            dish = getDishById(id);
            Logger.info("Dish was added:\n" + dish);
            return dish;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Dish wasn't added: " + dish + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteDishById(int id) {
        Dish dishToDel = getDishById(id);
        try {
            if (dishToDel == null) {
                throw new IllegalArgumentException("Dish with id '" + id + "' not found");
            }
            dishRepository.deleteDishById(id);

            Logger.info("Dish was deleted:\n" + dishToDel);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Dish wasn't deleted: " + dishToDel + "\n" + exception.getMessage());
            return false;
        }
    }

    @Override
    public Dish updateDishById(Dish dish, int id) {
        try {
            if (getDishById(id) == null) {
                throw new IllegalArgumentException("Dish with id '" + id + "' not found");
            }
            validator.validate(dish);
            dishRepository.updateDishById(dish, id);

            Logger.info("Dish was updated:\n" + dish);
            return getDishById(id);
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Dish wasn't updated to: " + dish + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public List<Dish> getDishes() {
        return dishRepository.getDishes();
    }

    @Override
    public int getDishCalories(int id) {
        if (getDishById(id) == null) {
            Logger.warn("Dish with id '" + id + "' not found");
            return 0;
        }
        return dishRepository.getDishCalories(id);
    }

    @Override
    public int getDishWeight(int id) {
        if (getDishById(id) == null) {
            Logger.warn("Dish with id '" + id + "' not found");
            return 0;
        }
        return dishRepository.getDishWeight(id);
    }

    @Override
    public List<Dish> getDishesByUserIdAndTypeOfMeal(int userId, TypeOfMeal typeOfMeal) {
        int calories = userParametersService.getUserCaloriesByTypeOfMeal(userId, typeOfMeal);

        if (calories == 0) {
            Logger.warn("Calories was 0: can't get dishes");
            return new ArrayList<>();
        }

        return dishRepository.getDishesByCaloriesRangeAndUserId(
            calories - 100,
            calories + 100,
            userId);
    }

    @Override
    public int getCountOfPages(int limit, String input) {
        try {
            pageValidator.validatePageLimit(limit);
        } catch (IllegalArgumentException exception) {
            Logger.warn(exception.getMessage());
            return 1;
        }
        double count = getDishesNameContainsInput(input).size() / (double) limit;

        int moreCount = (int) Math.ceil(count);
        return moreCount != 0 ? moreCount : 1;
    }

    @Override
    public List<Dish> getDishesNameSortedByPageAndInput(int limit, int pageNumber, String input) {
        try {
            pageValidator.validatePageLimit(limit);
            pageValidator.validatePageNumber(pageNumber, getCountOfPages(limit, input));

            return dishRepository.getDishesNameSortedByPageAndInput(limit, pageNumber, input);
        } catch (IllegalArgumentException exception) {
            Logger.warn(exception.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Dish> getDishesNameContainsInput(String input) {
        return getDishes().stream()
            .filter(dish -> dish.getName().toLowerCase().contains(input.toLowerCase()))
            .toList();
    }
}
