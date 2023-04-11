package edu.geekhub.homework.users;

import edu.geekhub.homework.users.interfaces.UserRepository;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserValidator userValidator;
    private static final String FETCH_ALL_USERS = """
        SELECT * FROM Users
        """;
    private static final String INSERT_USER = """
        INSERT INTO Users(firstName, lastName, password, email, role)
        VALUES (:firstName, :lastName, :password, :email, :role)
        """;
    private static final String FETCH_USER_BY_ID = """
        SELECT * FROM Users WHERE id=:id
        """;
    private static final String DELETE_USER_BY_ID = """
        DELETE FROM Users WHERE id=:id
        """;
    private static final String UPDATE_USER_BY_ID = """
        UPDATE Users SET
        firstName=:firstName, lastName=:lastName, password=:password, email=:email, role=:role
        WHERE id=:id
        """;
    private static final String FETCH_USER_BY_EMAIL = """
        SELECT * FROM Users WHERE email=:email
        """;
    private static final String FETCH_USERS_BY_ROLE = """
        SELECT * FROM Users WHERE role=:role
        """;

    public UserRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate,
                              UserValidator userValidator) {
        this.jdbcTemplate = jdbcTemplate;
        this.userValidator = userValidator;
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query(FETCH_ALL_USERS,
            (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("password"),
                rs.getString("email"),
                Role.valueOf(rs.getString("role"))
            ));
    }


    @Override
    public int addUser(User user) {
        userValidator.validateWithoutPass(user);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("firstName", user.getFirstName())
            .addValue("lastName", user.getLastName())
            .addValue("password", user.getPassword())
            .addValue("email", user.getEmail())
            .addValue("role", user.getRole().getName());

        jdbcTemplate.update(INSERT_USER, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }
        return -1;
    }

    @Override
    public User getUserById(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return jdbcTemplate.query(FETCH_USER_BY_ID, mapSqlParameterSource,
                (rs, rowNum) -> new User(
                    rs.getInt("id"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("password"),
                    rs.getString("email"),
                    Role.valueOf(rs.getString("role"))
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("email", email);

        return jdbcTemplate.query(FETCH_USER_BY_EMAIL, mapSqlParameterSource,
                (rs, rowNum) -> new User(
                    rs.getInt("id"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("password"),
                    rs.getString("email"),
                    Role.valueOf(rs.getString("role"))
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void deleteUserById(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        jdbcTemplate.update(DELETE_USER_BY_ID, mapSqlParameterSource);
    }

    @Override
    public void updateUserById(User user, int id) {
        userValidator.validateWithoutPass(user);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("firstName", user.getFirstName())
            .addValue("lastName", user.getLastName())
            .addValue("password", user.getPassword())
            .addValue("email", user.getEmail())
            .addValue("role", user.getRole().getName())
            .addValue("id", id);

        jdbcTemplate.update(UPDATE_USER_BY_ID, mapSqlParameterSource);
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("role", role.toString());

        return jdbcTemplate.query(FETCH_USERS_BY_ROLE, mapSqlParameterSource,
            (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("password"),
                rs.getString("email"),
                Role.valueOf(rs.getString("role"))
            ));
    }
}
