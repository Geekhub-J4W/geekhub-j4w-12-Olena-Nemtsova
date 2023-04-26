package edu.geekhub.coursework.allergics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.coursework.TestApplication;
import edu.geekhub.coursework.config.DatabaseContainer;
import edu.geekhub.coursework.config.DatabaseTestConfig;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {DatabaseTestConfig.class, TestApplication.class})
@Testcontainers
class UserAllergicProductRepositoryImplTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer =
        DatabaseContainer.getInstance();
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Mock
    private UserAllergicProductValidator validator;
    private UserAllergicProductRepositoryImpl userAllergicProductRepository;

    @BeforeEach
    void setUp() {
        userAllergicProductRepository = new UserAllergicProductRepositoryImpl(
            validator,
            jdbcTemplate
        );
    }

    @Test
    void can_get_user_allergic_product_relation() {
        UserAllergicProduct expectedRelation = new UserAllergicProduct(40, 3);

        UserAllergicProduct relation = userAllergicProductRepository.getRelationByUserAndProductId(
            3,
            40
        );

        assertEquals(expectedRelation, relation);
    }

    @Test
    void can_get_null_user_allergic_product_relation_by_wrong_id() {
        UserAllergicProduct relation = userAllergicProductRepository.getRelationByUserAndProductId(
            -1,
            -1
        );

        assertNull(relation);
    }

    @Test
    void can_add_user_allergic_product_relation() {
        UserAllergicProduct expectedRelation = new UserAllergicProduct(40, 1);

        userAllergicProductRepository.addRelation(expectedRelation);

        assertEquals(
            expectedRelation,
            userAllergicProductRepository.getRelationByUserAndProductId(1, 40)
        );
    }

    @Test
    void can_not_add_not_valid_user_allergic_product_relation() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> userAllergicProductRepository.addRelation(null)
        );
    }

    @Test
    void can_delete_user_allergic_product_relation() {
        userAllergicProductRepository.deleteRelationByUserAndProductId(3, 40);

        assertNull(
            userAllergicProductRepository.getRelationByUserAndProductId(3, 40)
        );
    }
}
