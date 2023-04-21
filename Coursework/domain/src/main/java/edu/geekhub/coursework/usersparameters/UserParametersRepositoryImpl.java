package edu.geekhub.coursework.usersparameters;

import edu.geekhub.coursework.usersparameters.interfaces.UserParametersRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserParametersRepositoryImpl implements UserParametersRepository {
    private final UserParametersValidator validator;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String INSERT_USER_PARAMETERS = """
        INSERT INTO UsersParameters
        (userId, age, weight, height, gender, activityLevel, bodyType, aim)
        VALUES (:userId, :age, :weight, :height, :gender, :activityLevel, :bodyType, :aim)
        """;
    private static final String FETCH_USER_PARAMETERS_BY_USER_ID = """
        SELECT * FROM UsersParameters WHERE userId=:userId
        """;
    private static final String DELETE_USER_PARAMETERS_BY_USER_ID = """
        DELETE FROM UsersParameters WHERE userId=:userId
        """;
    private static final String UPDATE_USER_PARAMETERS_BY_USER_ID = """
        UPDATE UsersParameters SET
        age=:age, weight=:weight, height=:height, gender=:gender,
        activityLevel=:activityLevel, bodyType=:bodyType, aim=:aim
        WHERE userId=:userId
        """;

    public UserParametersRepositoryImpl(
        UserParametersValidator validator,
        NamedParameterJdbcTemplate jdbcTemplate
    ) {
        this.validator = validator;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUserParameters(UserParameters userParameters) {
        validator.validate(userParameters);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("userId", userParameters.getUserId())
            .addValue("age", userParameters.getAge())
            .addValue("weight", userParameters.getWeight())
            .addValue("height", userParameters.getHeight())
            .addValue("gender", userParameters.getGender().name())
            .addValue("activityLevel", userParameters.getActivityLevel().name())
            .addValue("bodyType", userParameters.getBodyType().name())
            .addValue("aim", userParameters.getAim().name());

        jdbcTemplate.update(INSERT_USER_PARAMETERS, mapSqlParameterSource);
    }

    @Override
    public UserParameters getUserParametersByUserId(int userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("userId", userId);

        return jdbcTemplate.query(FETCH_USER_PARAMETERS_BY_USER_ID, mapSqlParameterSource,
                (rs, rowNum) -> new UserParameters(
                    rs.getInt("userId"),
                    rs.getInt("age"),
                    rs.getInt("weight"),
                    rs.getInt("height"),
                    Gender.valueOf(rs.getString("gender")),
                    ActivityLevel.valueOf(rs.getString("activityLevel")),
                    BodyType.valueOf(rs.getString("bodyType")),
                    Aim.valueOf(rs.getString("aim"))
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void deleteUserParametersByUserId(int userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("userId", userId);

        jdbcTemplate.update(DELETE_USER_PARAMETERS_BY_USER_ID, mapSqlParameterSource);
    }

    @Override
    public void updateUserParametersByUserId(UserParameters userParameters, int userId) {
        validator.validate(userParameters);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("age", userParameters.getAge())
            .addValue("weight", userParameters.getWeight())
            .addValue("height", userParameters.getHeight())
            .addValue("gender", userParameters.getGender().name())
            .addValue("activityLevel", userParameters.getActivityLevel().name())
            .addValue("bodyType", userParameters.getBodyType().name())
            .addValue("aim", userParameters.getAim().name())
            .addValue("userId", userId);

        jdbcTemplate.update(UPDATE_USER_PARAMETERS_BY_USER_ID, mapSqlParameterSource);
    }
}
