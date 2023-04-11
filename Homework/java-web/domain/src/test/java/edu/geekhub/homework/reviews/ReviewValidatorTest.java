package edu.geekhub.homework.reviews;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import edu.geekhub.homework.productsorders.ProductOrder;
import edu.geekhub.homework.productsorders.interfaces.ProductOrderRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewValidatorTest {
    private ReviewValidator reviewValidator;
    @Mock
    private ProductOrderRepository productOrderRepository;
    private Review review;

    @BeforeEach
    void setUp() {
        reviewValidator = new ReviewValidator(productOrderRepository);
        review = spy(
            new Review(1, LocalDateTime.now(), "some review", 1, 1, 1)
        );
    }

    @Test
    void can_not_validate_null_review() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> reviewValidator.validate(null)
        );

        assertEquals("Review was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_review_with_null_dateTime() {
        doReturn(null).when(review).getDateTime();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> reviewValidator.validate(review)
        );

        assertEquals("Review dateTime was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_order_with_dateTime_more_than_now() {
        LocalDateTime future = LocalDateTime.of(2050, 1, 1, 0, 0);
        doReturn(future).when(review).getDateTime();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> reviewValidator.validate(review)
        );

        assertEquals("Review dateTime was more than now", thrown.getMessage());
    }


    @Test
    void can_not_validate_review_with_null_text() {
        doReturn(null).when(review).getText();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> reviewValidator.validate(review)
        );

        assertEquals("Review text was null", thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        "0, Review rating was less than 1",
        "10, Review rating was more than 5"
    })
    void can_not_validate_product_with_wrong_price(Integer rating, String expectedMessage) {
        doReturn(rating).when(review).getRating();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> reviewValidator.validate(review)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_not_validate_review_by_not_exists_productOrder_relation() {
        doReturn(null)
            .when(productOrderRepository)
            .getRelationByProductAndOrderId(anyInt(), anyInt());
        String expectedMessage = "ProductOrder relation with productId '" + review.getProductId()
                                 + "' and orderId '" + review.getOrderId()
                                 + "' doesn't exist";

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> reviewValidator.validate(review)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_validate_correct_review() {
        doReturn(new ProductOrder(1, 1))
            .when(productOrderRepository)
            .getRelationByProductAndOrderId(anyInt(), anyInt());

        assertDoesNotThrow(
            () -> reviewValidator.validate(review)
        );
    }
}
