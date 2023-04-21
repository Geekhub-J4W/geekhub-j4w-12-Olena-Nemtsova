package edu.geekhub.coursework.dishes.interfaces;

import edu.geekhub.coursework.dishes.Dish;
import java.util.List;

public interface DishRepository {

    List<Dish> getDishes();

    int addDish(Dish dish);

    Dish getDishById(int id);

    void deleteDishById(int id);

    void updateDishById(Dish dish, int id);

    int getDishCalories(int id);

    int getDishWeight(int id);

    List<Dish> getDishesNameSortedByPageAndInput(int limit, int pageNumber, String input);

    List<Dish> getDishesByCaloriesRangeAndUserId(int minCalories, int maxCalories, int userId);
}
