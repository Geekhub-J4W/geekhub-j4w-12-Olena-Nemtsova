package edu.geekhub.coursework;

import edu.geekhub.coursework.dishes.Dish;
import edu.geekhub.coursework.dishes.interfaces.DishService;
import edu.geekhub.coursework.security.SecurityUser;
import edu.geekhub.coursework.util.TypeOfMeal;
import java.io.IOException;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public Dish addDish(@RequestBody Dish dish) {
        return dishService.addDish(dish);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public Dish updateDish(@RequestBody Dish dish, @PathVariable int id) {
        return dishService.updateDishById(dish, id);
    }

    @PutMapping("/image/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public Dish updateDishImage(
        @RequestParam("file") MultipartFile file,
        @PathVariable int id
    ) throws IOException {

        Dish dishToUpdate = dishService.getDishById(id);
        if (dishToUpdate != null) {
            dishToUpdate.setImage(file.getBytes());
        }
        return dishService.updateDishById(dishToUpdate, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public boolean deleteDish(@PathVariable int id) {
        return dishService.deleteDishById(id);
    }

    @GetMapping("/calories/{id}")
    public int getDishCalories(@PathVariable int id) {
        return dishService.getDishCalories(id);
    }

    @GetMapping("/weight/{id}")
    public int getDishWeight(@PathVariable int id) {
        return dishService.getDishWeight(id);
    }

    @GetMapping("/typeOfMeal/{typeOfMeal}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'USER')")
    public Collection<Dish> getUserDishesByTypeOfMeal(@PathVariable String typeOfMeal) {

        return dishService.getDishesByUserIdAndTypeOfMeal(
            getUserId(),
            TypeOfMeal.valueOf(typeOfMeal)
        );
    }

    @GetMapping("/{id}")
    public Dish getDishById(@PathVariable int id) {
        return dishService.getDishById(id);
    }

    @GetMapping("/pages/{limit}/{input}")
    public int getCountOfDishesPages(
        @PathVariable int limit,
        @PathVariable String input
    ) {
        input = input.equals("null") ? "" : input;
        return dishService.getCountOfPages(limit, input);
    }

    @GetMapping("/{limit}/{pageNumber}/{input}")
    public Collection<Dish> getDishesOfPageByInput(
        @PathVariable int limit,
        @PathVariable int pageNumber,
        @PathVariable String input
    ) {
        input = input.equals("null") ? "" : input;
        return dishService.getDishesNameSortedByPageAndInput(limit, pageNumber, input);
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
