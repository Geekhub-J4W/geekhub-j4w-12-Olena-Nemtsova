package edu.geekhub.coursework.dishes;

import edu.geekhub.coursework.dishes.interfaces.DishRepository;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class DishRepositoryImpl implements DishRepository {
    private final DishValidator validator;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String FETCH_ALL_DISHES = """
        SELECT * FROM Dishes
        """;
    private static final String INSERT_DISH = """
        INSERT INTO Dishes(name, image)
        VALUES (:name, :image)
        """;
    private static final String FETCH_DISH_BY_ID = """
        SELECT * FROM Dishes WHERE id=:id
        """;
    private static final String DELETE_DISH_BY_ID = """
        DELETE FROM Dishes WHERE id=:id
        """;
    private static final String UPDATE_DISH_BY_ID = """
        UPDATE Dishes SET
        name=:name, image=:image
        WHERE id=:id
        """;
    private static final String FETCH_DISHES_NAME_SORTED_BY_PAGE_AND_INPUT = """
        SELECT * FROM Dishes
        WHERE Dishes.name ILIKE :input
        ORDER BY Dishes.name
        LIMIT :limit
        OFFSET :limit * :pageNumber
        """;
    private static final String FETCH_DISH_CALORIES_BY_ID = """
        SELECT SUM(Products.calories * ProductsDishes.productQuantity / 100) AS calories
        FROM Products INNER JOIN ProductsDishes
        ON Products.id=ProductsDishes.productId
        WHERE ProductsDishes.dishId=:id
        """;

    private static final String FETCH_DISH_WEIGHT_BY_ID = """
        SELECT SUM(ProductsDishes.productQuantity) AS weight
        FROM ProductsDishes
        WHERE ProductsDishes.dishId=:id
        """;
    private static final String FETCH_DISHES_BY_CALORIES_RANGE_AND_USER_ID = """
        SELECT * FROM Dishes
        WHERE Dishes.id NOT IN (
        SELECT ProductsDishes.dishId FROM ProductsDishes
        WHERE ProductsDishes.productId IN
        (SELECT Products.id FROM Products
        INNER JOIN UsersAllergicProducts
        ON Products.id=UsersAllergicProducts.productId
        WHERE UsersAllergicProducts.userId=:userId)
        GROUP BY ProductsDishes.dishId
        )
        AND Dishes.id IN (
        SELECT ProductsDishes.dishId FROM ProductsDishes
        INNER JOIN Products
        ON ProductsDishes.productId=Products.id
        GROUP BY ProductsDishes.dishId
        HAVING SUM(Products.calories * ProductsDishes.productQuantity / 100)>=:minCalories
        AND SUM(Products.calories * ProductsDishes.productQuantity / 100)<=:maxCalories
        )
        """;

    public DishRepositoryImpl(DishValidator validator, NamedParameterJdbcTemplate jdbcTemplate) {
        this.validator = validator;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Dish> getDishes() {
        return jdbcTemplate.query(FETCH_ALL_DISHES,
            (rs, rowNum) -> new Dish(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getBytes("image")
            ));
    }

    @Override
    public int addDish(Dish dish) {
        validator.validate(dish);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("name", dish.getName())
            .addValue("image", dish.getImage());

        jdbcTemplate.update(INSERT_DISH, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }
        return -1;
    }

    @Override
    public Dish getDishById(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return jdbcTemplate.query(FETCH_DISH_BY_ID, mapSqlParameterSource,
                (rs, rowNum) -> new Dish(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getBytes("image")
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void deleteDishById(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        jdbcTemplate.update(DELETE_DISH_BY_ID, mapSqlParameterSource);
    }

    @Override
    public void updateDishById(Dish dish, int id) {
        validator.validate(dish);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("name", dish.getName())
            .addValue("image", dish.getImage())
            .addValue("id", id);

        jdbcTemplate.update(UPDATE_DISH_BY_ID, mapSqlParameterSource);
    }

    @Override
    public int getDishCalories(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return jdbcTemplate.query(FETCH_DISH_CALORIES_BY_ID, mapSqlParameterSource,
                (rs, rowNum) ->
                    rs.getInt("calories")
            )
            .stream()
            .findFirst()
            .orElse(0);
    }

    @Override
    public int getDishWeight(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return jdbcTemplate.query(FETCH_DISH_WEIGHT_BY_ID, mapSqlParameterSource,
                (rs, rowNum) ->
                    rs.getInt("weight")
            )
            .stream()
            .findFirst()
            .orElse(0);
    }

    @Override
    public List<Dish> getDishesNameSortedByPageAndInput(int limit, int pageNumber, String input) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("limit", limit)
            .addValue("pageNumber", pageNumber - 1)
            .addValue("input", "%" + input + "%");

        return jdbcTemplate.query(FETCH_DISHES_NAME_SORTED_BY_PAGE_AND_INPUT, mapSqlParameterSource,
            (rs, rowNum) -> new Dish(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getBytes("image")
            ));
    }

    @Override
    public List<Dish> getDishesByCaloriesRangeAndUserId(
        int minCalories,
        int maxCalories,
        int userId
    ) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("minCalories", minCalories)
            .addValue("maxCalories", maxCalories)
            .addValue("userId", userId);

        return jdbcTemplate.query(FETCH_DISHES_BY_CALORIES_RANGE_AND_USER_ID, mapSqlParameterSource,
            (rs, rowNum) -> new Dish(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getBytes("image")
            ));
    }
}
