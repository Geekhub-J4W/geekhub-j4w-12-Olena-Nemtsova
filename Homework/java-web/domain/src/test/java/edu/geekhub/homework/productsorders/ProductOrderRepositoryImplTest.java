package edu.geekhub.homework.productsorders;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
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
class ProductOrderRepositoryImplTest {
    private ProductOrderRepositoryImpl productOrderRepository;
    private final ProductOrder productOrder = new ProductOrder(1, 1);
    @Mock
    private ProductOrderValidator productOrderValidator;
    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        productOrderRepository = new ProductOrderRepositoryImpl(jdbcTemplate,
            productOrderValidator);
    }

    @Test
    void can_not_add_not_valid_productOrder_relation() {
        doThrow(new IllegalArgumentException()).when(productOrderValidator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> productOrderRepository.addProductOrder(null)
        );
    }

    @Test
    void can_not_add_not_valid_productOrder_relation_at_database_lvl() {
        doNothing().when(productOrderValidator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), any(), any());

        assertThrows(
            DataAccessException.class,
            () -> productOrderRepository.addProductOrder(productOrder)
        );
    }

    @Test
    void can_add_productOrder_relation() {
        doNothing().when(productOrderValidator).validate(any());
        doReturn(1).when(jdbcTemplate).update(anyString(), any(), any());

        assertDoesNotThrow(
            () -> productOrderRepository.addProductOrder(productOrder)
        );
    }

    @Test
    void can_get_productOrder_relations_list() {
        List<ProductOrder> products = List.of(productOrder);
        doReturn(products).when(jdbcTemplate).query(anyString(), any(RowMapper.class));
        assertDoesNotThrow(
            () -> productOrderRepository.getProductOrders()
        );
        assertEquals(products, productOrderRepository.getProductOrders());
    }

    @Test
    void can_get_productOrder_relation_by_product_and_order_id() {
        doReturn(List.of(productOrder))
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> productOrderRepository.getRelationByProductAndOrderId(1, 1)
        );
        assertEquals(productOrder, productOrderRepository.getRelationByProductAndOrderId(1, 1));
    }

    @Test
    void can_get_null_productOrder_relation_by_wrong_product_or_order_id() {
        doReturn(new ArrayList<>())
            .when(jdbcTemplate)
            .query(anyString(), (SqlParameterSource) any(), any(RowMapper.class));

        assertDoesNotThrow(
            () -> productOrderRepository.getRelationByProductAndOrderId(1, 1)
        );
        assertNull(productOrderRepository.getRelationByProductAndOrderId(1, 1));
    }

    @Test
    void can_not_delete_productOrder_relation_by_order_id_not_deleted_at_database() {
        doThrow(new DataAccessException("") {
        }).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertThrows(
            DataAccessException.class,
            () -> productOrderRepository.deleteRelationsByOrderId(1)
        );
    }

    @Test
    void can_delete_productOrder_relation_by_order_id() {
        doReturn(1).when(jdbcTemplate).update(anyString(), (SqlParameterSource) any());

        assertDoesNotThrow(
            () -> productOrderRepository.deleteRelationsByOrderId(1)
        );
    }
}
