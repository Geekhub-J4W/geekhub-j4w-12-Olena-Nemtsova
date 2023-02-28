package edu.geekhub.homework.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@PropertySource("classpath:database.properties")
public class DatabaseConfig {

    @Bean
    public HikariDataSource hikariDataSource(@Value("${database.username}") String username,
                                             @Value("${database.password}") String password,
                                             @Value("${database.url}") String url,
                                             @Value("${database.pool.size}") int maxPoolSize,
                                             @Value("${database.idleTimeout}") int idleTimeout,
                                             @Value("${database.driver}") String driver) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(maxPoolSize);
        hikariConfig.setMinimumIdle(0);
        hikariConfig.setIdleTimeout(idleTimeout);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public NamedParameterJdbcTemplate researchCenterNamedJdbc(HikariDataSource hikariDataSource) {
        return new NamedParameterJdbcTemplate(hikariDataSource);
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(HikariDataSource dataSource) {
        return Flyway.configure()
            .dataSource(dataSource)
            .outOfOrder(true)
            .locations("classpath:/db/flyway")
            .cleanDisabled(true)
            .baselineOnMigrate(true)
            .load();
    }
}
