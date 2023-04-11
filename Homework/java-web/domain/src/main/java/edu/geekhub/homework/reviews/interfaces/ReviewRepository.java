package edu.geekhub.homework.reviews.interfaces;

import edu.geekhub.homework.reviews.Review;
import java.util.List;

public interface ReviewRepository {
    List<Review> getReviews();

    List<Review> getReviewsByProductId(int productId);

    int addReview(Review review);

    Review getReviewByProductAndOrderId(int productId, int orderId);

    Review getReviewById(int id);

    void updateReviewById(Review review, int id);
}
