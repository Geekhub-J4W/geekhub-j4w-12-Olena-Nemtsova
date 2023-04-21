package edu.geekhub.coursework.allergics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.coursework.allergics.interfaces.UserAllergicProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class UserAllergicProductServiceImplTest {
    private UserAllergicProductServiceImpl userAllergicProductService;
    @Mock
    private UserAllergicProductValidator validator;
    @Mock
    private UserAllergicProductRepository repository;
    private UserAllergicProduct userAllergicProduct;

    @BeforeEach
    void setUp() {
        userAllergicProductService = new UserAllergicProductServiceImpl(validator, repository);

        userAllergicProduct = new UserAllergicProduct(1, 1);
    }

    @Test
    void can_get_userAllergicProduct_by_userId_and_productId() {
        doReturn(userAllergicProduct)
            .when(repository).getRelationByUserAndProductId(anyInt(), anyInt());

        UserAllergicProduct gotUserAllergicProduct = userAllergicProductService
            .getRelationByUserAndProductId(1, 1);

        assertEquals(userAllergicProduct, gotUserAllergicProduct);
    }

    @Test
    void can_add_userAllergicProduct() {
        doNothing().when(validator).validate(any());
        doNothing().when(repository).addRelation(any());
        doReturn(null, userAllergicProduct)
            .when(repository).getRelationByUserAndProductId(anyInt(), anyInt());

        UserAllergicProduct addedUserAllergicProduct = userAllergicProductService
            .addRelation(userAllergicProduct);

        assertNotNull(addedUserAllergicProduct);
    }

    @Test
    void can_not_add_not_valid_userAllergicProduct() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        UserAllergicProduct addedUserAllergicProduct = userAllergicProductService
            .addRelation(userAllergicProduct);

        assertNull(addedUserAllergicProduct);
    }

    @Test
    void can_not_add_existing_userAllergicProduct() {
        doNothing().when(validator).validate(any());
        doReturn(userAllergicProduct)
            .when(repository).getRelationByUserAndProductId(anyInt(), anyInt());

        UserAllergicProduct addedUserAllergicProduct = userAllergicProductService
            .addRelation(userAllergicProduct);

        assertNull(addedUserAllergicProduct);
    }

    @Test
    void can_not_add_userAllergicProduct_not_added_at_repository() {
        doNothing().when(validator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(repository).getRelationByUserAndProductId(anyInt(), anyInt());

        UserAllergicProduct addedUserAllergicProduct = userAllergicProductService
            .addRelation(userAllergicProduct);

        assertNull(addedUserAllergicProduct);
    }


    @Test
    void can_delete_userAllergicProduct_by_userId_and_productId() {
        doReturn(userAllergicProduct).when(repository)
            .getRelationByUserAndProductId(anyInt(), anyInt());
        doNothing().when(repository).deleteRelationByUserAndProductId(anyInt(), anyInt());

        boolean successfulDeleted = userAllergicProductService
            .deleteRelationByUserAndProductId(1, 1);

        assertTrue(successfulDeleted);
    }

    @Test
    void can_not_delete_not_existing_userAllergicProduct() {
        doReturn(null).when(repository)
            .getRelationByUserAndProductId(anyInt(), anyInt());

        boolean successfulDeleted = userAllergicProductService
            .deleteRelationByUserAndProductId(1, 1);

        assertFalse(successfulDeleted);
    }

    @Test
    void can_not_delete_userAllergicProduct_not_deleted_at_repository() {
        doReturn(userAllergicProduct).when(repository)
            .getRelationByUserAndProductId(anyInt(), anyInt());
        doThrow(new DataAccessException("") {
        }).when(repository).deleteRelationByUserAndProductId(anyInt(), anyInt());

        boolean successfulDeleted = userAllergicProductService
            .deleteRelationByUserAndProductId(1, 1);

        assertFalse(successfulDeleted);
    }
}
