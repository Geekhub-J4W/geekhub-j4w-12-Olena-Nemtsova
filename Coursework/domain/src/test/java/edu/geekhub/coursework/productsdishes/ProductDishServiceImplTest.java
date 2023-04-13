package edu.geekhub.coursework.productsdishes;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.coursework.productsdishes.interfaces.ProductDishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class ProductDishServiceImplTest {
    private ProductDishServiceImpl productDishService;
    @Mock
    private ProductDishValidator validator;
    @Mock
    private ProductDishRepository repository;
    private ProductDish productDish;

    @BeforeEach
    void setUp() {
        productDishService = new ProductDishServiceImpl(validator, repository);

        productDish = new ProductDish(1, 1, 80);
    }

    @Test
    void can_get_productDish_relation_by_productId_and_dishId() {
        doReturn(productDish).when(repository).getRelationByProductAndDishId(anyInt(), anyInt());

        ProductDish gotProductDish = productDishService.getRelationByProductAndDishId(1, 1);

        assertNotNull(gotProductDish);
    }

    @Test
    void can_add_productDish_relation() {
        doNothing().when(validator).validate(any());
        doNothing().when(repository).addRelation(any());
        doReturn(productDish).when(repository).getRelationByProductAndDishId(anyInt(), anyInt());

        ProductDish addedProductDish = productDishService.addRelation(productDish);

        assertNotNull(addedProductDish);
    }

    @Test
    void can_not_add_not_valid_productDish_relation() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        ProductDish addedProductDish = productDishService.addRelation(productDish);

        assertNull(addedProductDish);
    }

    @Test
    void can_not_add_productDish_relation_not_added_at_repository() {
        doNothing().when(validator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(repository).addRelation(any());

        ProductDish addedProductDish = productDishService.addRelation(productDish);

        assertNull(addedProductDish);
    }

    @Test
    void can_update_productDish_relation() {
        doReturn(productDish).when(repository).getRelationByProductAndDishId(anyInt(), anyInt());
        doNothing().when(validator).validate(any());
        doNothing().when(repository).updateRelationByProductAndDishId(any());

        ProductDish updatedProductDish = productDishService.updateRelation(productDish);

        assertNotNull(updatedProductDish);
    }

    @Test
    void can_not_update_not_existing_productDish_relation() {
        doReturn(null).when(repository).getRelationByProductAndDishId(anyInt(), anyInt());

        ProductDish updatedProductDish = productDishService.updateRelation(productDish);

        assertNull(updatedProductDish);
    }

    @Test
    void can_not_update_productDish_relation_to_wrong() {
        doReturn(productDish).when(repository).getRelationByProductAndDishId(anyInt(), anyInt());
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        ProductDish updatedProductDish = productDishService.updateRelation(productDish);

        assertNull(updatedProductDish);
    }

    @Test
    void can_not_update_productDish_relation_not_updated_at_repository() {
        doReturn(productDish).when(repository).getRelationByProductAndDishId(anyInt(), anyInt());
        doNothing().when(validator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(repository).updateRelationByProductAndDishId(any());

        ProductDish updatedProductDish = productDishService.updateRelation(productDish);

        assertNull(updatedProductDish);
    }

    @Test
    void can_delete_productDish_relation_by_productId_and_dishId() {
        doReturn(productDish).when(repository).getRelationByProductAndDishId(anyInt(), anyInt());
        doNothing().when(repository).deleteRelationByProductAndDishId(anyInt(), anyInt());

        boolean successfulDeleted = productDishService.deleteRelationByProductAndDishId(1, 1);

        assertTrue(successfulDeleted);
    }

    @Test
    void can_not_delete_not_existing_productDish_relation() {
        doReturn(null).when(repository).getRelationByProductAndDishId(anyInt(), anyInt());

        boolean successfulDeleted = productDishService.deleteRelationByProductAndDishId(1, 1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_not_delete_productDish_relation_not_deleted_at_repository() {
        doReturn(productDish).when(repository).getRelationByProductAndDishId(anyInt(), anyInt());
        doThrow(new DataAccessException("") {
        }).when(repository).deleteRelationByProductAndDishId(anyInt(), anyInt());

        boolean successfulDeleted = productDishService.deleteRelationByProductAndDishId(1, 1);

        assertFalse(successfulDeleted);
    }
}
