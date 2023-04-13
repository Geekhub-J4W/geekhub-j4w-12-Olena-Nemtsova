package edu.geekhub.coursework.dishes.interfaces;

import edu.geekhub.coursework.dishes.Dish;
import edu.geekhub.coursework.util.TypeOfMeal;
import java.util.List;

public interface DishService {
    Dish getDishById(int id);

    Dish addDish(Dish dish);

    boolean deleteDishById(int id);

    Dish updateDishById(Dish dish, int id);

    List<Dish> getDishes();

    List<Dish> getDishesByUserIdAndTypeOfMeal(int userId, TypeOfMeal typeOfMeal);

    int getCountOfPages(int limit, String input);

    List<Dish> getDishesNameSortedByPageAndInput(int limit, int pageNumber, String input);
}
