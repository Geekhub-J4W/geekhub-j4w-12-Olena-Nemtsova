package edu.geekhub.homework.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.homework.domain.Product;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@ExtendWith(MockitoExtension.class)
class BucketRepositoryImplTest {
    private BucketRepositoryImpl bucketRepository;
    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        bucketRepository = new BucketRepositoryImpl(jdbcTemplate);
    }

    @Test
    void can_not_add_product_to_bucket_by_wrong_parameters() {
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), any(), any());

        assertThrows(
            DataAccessException.class,
            () -> bucketRepository.addBucketProduct(-1, "")
        );
    }

    @Test
    void can_add_product_to_bucket() {
        doReturn(1).when(jdbcTemplate).update(anyString(), any(), any());

        assertDoesNotThrow(
            () -> bucketRepository.addBucketProduct(1, "")
        );
    }

    @Test
    void can_not_delete_user_bucket_products_by_wrong_id() {
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertThrows(
            DataAccessException.class,
            () -> bucketRepository.deleteUserBucketProductsById("")
        );
    }

    @Test
    void can_delete_user_bucket_products() {
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> bucketRepository.deleteUserBucketProductsById("userId1")
        );
    }

    @Test
    void can_not_delete_user_bucket_product_by_wrong_ids() {
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertThrows(
            DataAccessException.class,
            () -> bucketRepository.deleteUserBucketProductById(-1, "")
        );
    }

    @Test
    void can_delete_user_bucket_product() {
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> bucketRepository.deleteUserBucketProductById(1, "userId1")
        );
    }

    @Test
    void can_get_bucket_products_by_user_id() {
        List<Product> products = List.of(new Product());
        doReturn(products).when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> bucketRepository.getBucketProductsByUserId("userId1")
        );
        assertEquals(products, bucketRepository.getBucketProductsByUserId("userId1"));
    }
}
