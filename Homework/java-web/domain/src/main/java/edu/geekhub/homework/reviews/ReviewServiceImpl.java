package edu.geekhub.homework.reviews;

import edu.geekhub.homework.reviews.interfaces.ReviewRepository;
import edu.geekhub.homework.reviews.interfaces.ReviewService;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewValidator validator;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             ReviewValidator validator) {
        this.reviewRepository = reviewRepository;
        this.validator = validator;
    }

    @Override
    public Review getReviewByProductAndOrderId(int productId, int orderId) {
        return reviewRepository.getReviewByProductAndOrderId(productId, orderId);
    }

    @Override
    public List<Review> getReviewsByProductId(int productId) {
        return reviewRepository.getReviewsByProductId(productId);
    }

    @Override
    public List<Review> getReviews() {
        return reviewRepository.getReviews();
    }

    @Override
    public Review addReview(Review review) {
        try {
            validator.validate(review);
            int id = reviewRepository.addReview(review);
            if (id == -1) {
                throw new IllegalArgumentException("Unable to retrieve the generated key");
            }
            review.setId(id);
            Logger.info("Review was added:\n" + review);
            return getReviewById(id);
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Review wasn't added: " + review + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public Review updateReviewById(Review review, int id) {
        try {
            validator.validate(review);
            if (getReviewById(id) == null) {
                throw new IllegalArgumentException("Review with id '" + id
                                                   + "' not found");
            }
            reviewRepository.updateReviewById(review, id);
            Logger.info("Review was updated:\n" + review);
            return getReviewById(id);
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Review wasn't updated: " + review + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public Review getReviewById(int id) {
        return reviewRepository.getReviewById(id);
    }

    @Override
    public double getSeveralRatingByProductId(int productId) {
        return getReviewsByProductId(productId)
            .stream()
            .mapToDouble(Review::getRating)
            .average()
            .orElse(0);
    }
}
