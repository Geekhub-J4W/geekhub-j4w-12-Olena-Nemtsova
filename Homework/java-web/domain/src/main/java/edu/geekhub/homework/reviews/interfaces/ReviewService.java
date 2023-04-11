package edu.geekhub.homework.reviews.interfaces;

import edu.geekhub.homework.reviews.Review;
import java.util.List;

public interface ReviewService {
    Review getReviewByProductAndOrderId(int productId, int orderId);

    List<Review> getReviewsByProductId(int productId);

    List<Review> getReviews();

    Review addReview(Review review);

    Review updateReviewById(Review review, int id);

    Review getReviewById(int id);

    double getSeveralRatingByProductId(int productId);
}
