package edu.geekhub.homework;

import edu.geekhub.homework.reviews.Review;
import edu.geekhub.homework.reviews.interfaces.ReviewService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{productId}")
    public List<Review> getProductReviews(@PathVariable int productId) {
        return reviewService.getReviewsByProductId(productId);
    }

    @GetMapping("/rating/{productId}")
    public double getProductRating(@PathVariable int productId) {
        return reviewService.getSeveralRatingByProductId(productId);
    }

    @GetMapping("/{productId}/{orderId}")
    public Review getReview(@PathVariable int productId,
                                        @PathVariable int orderId) {
        return reviewService.getReviewByProductAndOrderId(productId, orderId);
    }

    @PostMapping
    public Review addReview(@RequestBody Review review) {
        Review newReview = reviewService.addReview(review);

        if (review == null) {
            throw new IllegalArgumentException("Review wasn't added");
        }
        return newReview;
    }

    @PostMapping("/{id}")
    public Review updateReview(@RequestBody Review review,
                            @PathVariable int id) {

        Review updatedReview = reviewService.updateReviewById(review, id);
        if (updatedReview == null) {
            throw new IllegalArgumentException("Review wasn't updated");
        }

        return updatedReview;
    }
}
