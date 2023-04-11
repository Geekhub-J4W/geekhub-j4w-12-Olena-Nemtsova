package edu.geekhub.homework.reviews;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import edu.geekhub.homework.reviews.interfaces.ReviewRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {
    private ReviewServiceImpl reviewService;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewValidator reviewValidator;
    private Review review;

    @BeforeEach
    void setUp() {
        reviewService = new ReviewServiceImpl(reviewRepository, reviewValidator);
        review = new Review(1, LocalDateTime.now(), "Good product", 5, 1, 1);
    }

    @Test
    void can_get_all_reviews() {
        List<Review> expectedReviews = List.of(review);
        doReturn(expectedReviews).when(reviewRepository).getReviews();

        List<Review> reviews = reviewService.getReviews();

        assertEquals(expectedReviews, reviews);
    }

    @Test
    void can_get_reviews_by_product_id() {
        List<Review> expectedReviews = List.of(review);
        doReturn(expectedReviews).when(reviewRepository).getReviewsByProductId(anyInt());

        List<Review> reviews = reviewService.getReviewsByProductId(1);

        assertEquals(expectedReviews, reviews);
    }

    @Test
    void can_get_review_by_id() {
        doReturn(review).when(reviewRepository).getReviewById(anyInt());

        Review gotReview = reviewService.getReviewById(1);

        assertEquals(review, gotReview);
    }

    @Test
    void can_get_null_review_by_wrong_id() {
        doReturn(null).when(reviewRepository).getReviewById(anyInt());

        Review gotReview = reviewService.getReviewById(0);

        assertNull(gotReview);
    }

    @Test
    void can_get_review_by_product_and_order_id() {
        doReturn(review).when(reviewRepository).getReviewByProductAndOrderId(anyInt(), anyInt());

        Review gotReview = reviewService.getReviewByProductAndOrderId(1, 1);

        assertEquals(review, gotReview);
    }

    @Test
    void can_add_review() {
        doNothing().when(reviewValidator).validate(any());
        doReturn(1).when(reviewRepository).addReview(any());
        doReturn(review).when(reviewRepository).getReviewById(anyInt());

        Review addedReview = reviewService.addReview(review);

        assertNotNull(addedReview);
    }

    @Test
    void can_not_add_review_not_get_review_id_from_repository() {
        doNothing().when(reviewValidator).validate(any());
        doReturn(-1).when(reviewRepository).addReview(any());

        Review addedReview = reviewService.addReview(review);

        assertNull(addedReview);
    }

    @Test
    void can_not_add_review_not_added_at_repository() {
        doNothing().when(reviewValidator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(reviewRepository).addReview(any());

        Review addedReview = reviewService.addReview(review);

        assertNull(addedReview);
    }

    @Test
    void can_not_add_not_valid_review() {
        doThrow(new IllegalArgumentException()).when(reviewValidator).validate(any());

        Review addedReview = reviewService.addReview(null);

        assertNull(addedReview);
    }

    @Test
    void can_update_review_by_id() {
        doNothing().when(reviewValidator).validate(any());
        doReturn(review).when(reviewRepository).getReviewById(anyInt());
        doNothing().when(reviewRepository).updateReviewById(any(), anyInt());

        Review updatedReview = reviewService.updateReviewById(review, 1);

        assertNotNull(updatedReview);
    }

    @Test
    void can_not_update_review_to_not_valid_review() {
        doThrow(new IllegalArgumentException()).when(reviewValidator).validate(any());

        Review updatedReview = reviewService.updateReviewById(review, 1);

        assertNull(updatedReview);
    }

    @Test
    void can_not_update_review_by_not_existing_id() {
        doNothing().when(reviewValidator).validate(any());
        doReturn(null).when(reviewRepository).getReviewById(anyInt());

        Review updatedReview = reviewService.updateReviewById(review, 1);

        assertNull(updatedReview);
    }

    @Test
    void can_not_update_review_not_updated_at_repository() {
        doNothing().when(reviewValidator).validate(any());
        doReturn(review).when(reviewRepository).getReviewById(anyInt());
        doThrow(new DataAccessException("") {
        })
            .when(reviewRepository).updateReviewById(any(), anyInt());

        Review updatedReview = reviewService.updateReviewById(review, 1);

        assertNull(updatedReview);
    }

    @Test
    void can_get_several_reviews_rating_by_product_id() {
        Review review2 = new Review(1, LocalDateTime.now(), "Bad product", 1, 1, 1);
        List<Review> reviewsOfProduct = List.of(review, review2);
        reviewService = spy(reviewService);
        doReturn(reviewsOfProduct).when(reviewService).getReviewsByProductId(anyInt());

        double severalProductRating = reviewService.getSeveralRatingByProductId(1);

        assertEquals(3.0, severalProductRating);
    }

    @Test
    void can_get_zero_several_reviews_rating_by_product_id_if_reviews_not_exists() {
        reviewService = spy(reviewService);
        doReturn(new ArrayList<Review>()).when(reviewService).getReviewsByProductId(anyInt());

        double severalProductRating = reviewService.getSeveralRatingByProductId(1);

        assertEquals(0, severalProductRating);
    }
}
