package edu.geekhub.homework.repository;

import edu.geekhub.homework.domain.User;
import edu.geekhub.homework.domain.UserValidator;
import edu.geekhub.homework.repository.interfaces.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class UserRepositoryImpl implements UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserValidator userValidator;
    private static final String FETCH_ALL_USERS = """
        SELECT * FROM Users
        """;
    private static final String INSERT_USER = """
        INSERT INTO Users(id, firstName, lastName, password, email, isAdmin)
        VALUES (:id, :firstName, :lastName, :password, :email, :isAdmin)
        """;
    private static final String FETCH_USER_BY_ID = """
        SELECT * FROM Users WHERE id=:id
        """;
    private static final String DELETE_USER_BY_ID = """
        DELETE FROM Users WHERE id=:id
        """;
    private static final String UPDATE_USER_BY_ID = """
        UPDATE Users SET
        firstName=:firstName, lastName=:lastName, password=:password, email=:email, isAdmin=:isAdmin
        WHERE id=:id
        """;

    public UserRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate,
                              UserValidator userValidator) {
        this.jdbcTemplate = jdbcTemplate;
        this.userValidator = userValidator;
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query(FETCH_ALL_USERS,
            (resultSet, rowNum) -> new User(
                resultSet.getString("id"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("password"),
                resultSet.getString("email"),
                resultSet.getBoolean("isAdmin")
            ));
    }


    @Override
    public String addUser(User user) {
        userValidator.validate(user);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", UUID.randomUUID().toString())
            .addValue("firstName", user.getFirstName())
            .addValue("lastName", user.getLastName())
            .addValue("password", user.getPassword())
            .addValue("email", user.getEmail())
            .addValue("isAdmin", user.isAdmin());

        jdbcTemplate.update(INSERT_USER, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (String) keys.get("id");
        }
        return null;
    }

    @Override
    public User getUserById(String id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return jdbcTemplate.query(FETCH_USER_BY_ID, mapSqlParameterSource,
                (resultSet, rowNum) -> new User(
                    resultSet.getString("id"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("password"),
                    resultSet.getString("email"),
                    resultSet.getBoolean("isAdmin")
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void deleteUserById(String id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        jdbcTemplate.update(DELETE_USER_BY_ID, mapSqlParameterSource);
    }

    @Override
    public void updateUserById(User user, String id) {
        userValidator.validate(user);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("firstName", user.getFirstName())
            .addValue("lastName", user.getLastName())
            .addValue("password", user.getPassword())
            .addValue("email", user.getEmail())
            .addValue("isAdmin", user.isAdmin())
            .addValue("id", id);

        jdbcTemplate.update(UPDATE_USER_BY_ID, mapSqlParameterSource);
    }
}
