package edu.geekhub.homework.reviews;

import edu.geekhub.homework.productsorders.interfaces.ProductOrderRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class ReviewValidator {
    private final ProductOrderRepository productOrderRepository;

    public ReviewValidator(ProductOrderRepository productOrderRepository) {
        this.productOrderRepository = productOrderRepository;
    }

    public void validate(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("Review was null");
        }
        validateDateTime(review.getDateTime());
        validateReviewText(review.getText());
        validateRating(review.getRating());
        validateProductOrderExists(review.getProductId(), review.getOrderId());
    }

    private void validateDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Review dateTime was null");
        }
        if (dateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Review dateTime was more than now");
        }
    }

    private void validateReviewText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Review text was null");
        }
    }

    private void validateRating(int rating) {
        if (rating < 1) {
            throw new IllegalArgumentException("Review rating was less than 1");
        }
        if (rating > 5) {
            throw new IllegalArgumentException("Review rating was more than 5");
        }
    }

    private void validateProductOrderExists(int productId, int orderId) {
        if (productOrderRepository.getRelationByProductAndOrderId(productId, orderId) == null) {
            throw new IllegalArgumentException("ProductOrder relation with productId '" + productId
                                               + "' and orderId '" + orderId
                                               + "' doesn't exist");
        }
    }
}
