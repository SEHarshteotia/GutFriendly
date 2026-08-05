package com.gutfriendly.app.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gutfriendly.app.user.dto.ReviewDTO;
import com.gutfriendly.app.user.dto.ReviewRequestDTO;
import com.gutfriendly.app.user.dto.ReviewSummaryDTO;
import com.gutfriendly.app.user.enums.OrderStatus;
import com.gutfriendly.app.user.enums.ReviewKeyword;
import com.gutfriendly.app.user.enums.ReviewType;
import com.gutfriendly.app.user.exception.BadRequestException;
import com.gutfriendly.app.user.exception.ConflictException;
import com.gutfriendly.app.user.exception.ResourceNotFoundException;
import com.gutfriendly.app.user.model.CustomerOrder;
import com.gutfriendly.app.user.model.ShopDetails;
import com.gutfriendly.app.user.model.ShopReview;
import com.gutfriendly.app.user.model.UserDetails;
import com.gutfriendly.app.user.repository.CustomerOrderRepository;
import com.gutfriendly.app.user.repository.ShopDetailsRepository;
import com.gutfriendly.app.user.repository.ShopReviewRepository;
import com.gutfriendly.app.user.repository.UserRepo;

@Service
public class ReviewService {

    @Autowired
    private ShopReviewRepository reviewRepo;

    @Autowired
    private CustomerOrderRepository orderRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ShopDetailsRepository shopRepo;

    @Autowired
    private GutTrustScoreService gutTrustScoreService;

    // Adds a verified review for a delivered order.
    @Transactional
    public ReviewDTO addReview(
            int userId,
            ReviewRequestDTO request) {

        validateRating(request.getRating());

        UserDetails user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        CustomerOrder order =
                orderRepo.findById(request.getOrderId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                ));

        validateOrderOwnership(order, userId);

        if (order.getOrderStatus() != OrderStatus.DELIVERED) {
            throw new BadRequestException(
                    "Review can be submitted only after order delivery"
            );
        }

        if (reviewRepo.existsByOrderOrderId(
                order.getOrderId())) {

            throw new ConflictException(
                    "Review has already been submitted for this order"
            );
        }

        ReviewType reviewType =
                determineReviewType(request);

        int points =
                calculateRewardPoints(request);

        ShopReview review = new ShopReview();

        review.setOrder(order);
        review.setUser(user);
        review.setShop(order.getShop());

        review.setRating(request.getRating());

        review.setComment(
                cleanComment(request.getComment())
        );

        review.setKeywords(
                cleanKeywords(request.getKeywords())
        );

        review.setReviewType(reviewType);
        review.setPointsAwarded(points);

        // Marks that reward points are granted for this review.
        review.setRewardGranted(true);

        ShopReview savedReview =
                reviewRepo.saveAndFlush(review);

        // Reward is granted only on first submission.
        user.setRewardPoints(
                user.getRewardPoints() + points
        );

        userRepo.save(user);

        int shopId =
                order.getShop().getShopId();

        updateShopUserTrustScore(shopId);

        gutTrustScoreService
                .recalculateFinalScore(shopId);

        return convertToDTO(savedReview);
    }

    // Displays active reviews of one shop with pagination.
    @Transactional(readOnly = true)
    public Page<ReviewDTO> getReviewsByShop(
            int shopId,
            Pageable pageable) {

        if (!shopRepo.existsById(shopId)) {
            throw new ResourceNotFoundException(
                    "Shop not found"
            );
        }

        return reviewRepo
                .findByShopShopIdAndActiveTrue(
                        shopId,
                        pageable
                )
                .map(this::convertToDTO);
    }

    
 // Returns the rating summary of one shop.
    @Transactional(readOnly = true)
    public ReviewSummaryDTO getReviewSummary(
            int shopId) {

        if (!shopRepo.existsById(shopId)) {
            throw new ResourceNotFoundException(
                    "Shop not found"
            );
        }

        Double averageRating =
                reviewRepo.calculateAverageRating(
                        shopId
                );

        if (averageRating == null) {
            averageRating = 0.0;
        }

        averageRating =
                Math.round(
                        averageRating * 10.0
                ) / 10.0;

        long totalReviews =
                reviewRepo
                        .countByShopShopIdAndActiveTrue(
                                shopId
                        );

        long fiveStar =
                reviewRepo
                        .countByShopShopIdAndRatingAndActiveTrue(
                                shopId,
                                5
                        );

        long fourStar =
                reviewRepo
                        .countByShopShopIdAndRatingAndActiveTrue(
                                shopId,
                                4
                        );

        long threeStar =
                reviewRepo
                        .countByShopShopIdAndRatingAndActiveTrue(
                                shopId,
                                3
                        );

        long twoStar =
                reviewRepo
                        .countByShopShopIdAndRatingAndActiveTrue(
                                shopId,
                                2
                        );

        long oneStar =
                reviewRepo
                        .countByShopShopIdAndRatingAndActiveTrue(
                                shopId,
                                1
                        );

        return new ReviewSummaryDTO(
                averageRating,
                totalReviews,
                fiveStar,
                fourStar,
                threeStar,
                twoStar,
                oneStar
        );
    }
    
    // Returns one review by its ID.
    @Transactional(readOnly = true)
    public ReviewDTO getReviewById(
            int reviewId) {

        ShopReview review =
                reviewRepo.findById(reviewId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Review not found"
                                ));

        return convertToDTO(review);
    }

    // Allows the user to update their own review.
    // Reward points remain unchanged.
    @Transactional
    public ReviewDTO updateReview(
            int userId,
            int reviewId,
            ReviewRequestDTO request) {

        validateRating(request.getRating());

        ShopReview review =
                reviewRepo.findById(reviewId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Review not found"
                                ));

        validateReviewOwnership(
                review,
                userId
        );

        review.setRating(
                request.getRating()
        );

        review.setComment(
                cleanComment(
                        request.getComment()
                )
        );

        review.setKeywords(
                cleanKeywords(
                        request.getKeywords()
                )
        );

        review.setReviewType(
                determineReviewType(request)
        );

        // Do not update pointsAwarded.
        // Do not update rewardGranted.
        ShopReview updatedReview =
                reviewRepo.saveAndFlush(review);

        int shopId =
                review.getShop().getShopId();

        updateShopUserTrustScore(shopId);

        gutTrustScoreService
                .recalculateFinalScore(shopId);

        return convertToDTO(updatedReview);
    }

    // Soft deletes the user's own review.
    // Reward points remain unchanged.
    @Transactional
    public void deleteReview(
            int userId,
            int reviewId) {

        ShopReview review =
                reviewRepo.findById(reviewId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Review not found"
                                ));

        validateReviewOwnership(
                review,
                userId
        );

        int shopId =
                review.getShop().getShopId();

        review.setActive(false);

        // Do not remove reward points.
        // Do not change rewardGranted.
        reviewRepo.saveAndFlush(review);

        updateShopUserTrustScore(shopId);

        gutTrustScoreService
                .recalculateFinalScore(shopId);
    }

    // Rating must be between 1 and 5.
    private void validateRating(
            int rating) {

        if (rating < 1 || rating > 5) {
            throw new BadRequestException(
                    "Rating must be between 1 and 5"
            );
        }
    }

    // Determines whether the review is basic, rapid, or detailed.
    private ReviewType determineReviewType(
            ReviewRequestDTO request) {

        String comment =
                cleanComment(
                        request.getComment()
                );

        if (comment != null &&
                comment.length() >= 100) {

            return ReviewType.DETAILED;
        }

        if (request.getKeywords() != null &&
                !request.getKeywords().isEmpty()) {

            return ReviewType.RAPID;
        }

        return ReviewType.BASIC;
    }

    // Calculates reward points during first submission.
    private int calculateRewardPoints(
            ReviewRequestDTO request) {

        String comment =
                cleanComment(
                        request.getComment()
                );

        int commentLength =
                comment == null
                        ? 0
                        : comment.length();

        if (commentLength >= 200) {
            return 20;
        }

        if (commentLength >= 100) {
            return 15;
        }

        if (request.getKeywords() != null &&
                !request.getKeywords().isEmpty()) {

            return 10;
        }

        return 5;
    }

    // Recalculates average user rating of one shop.
    private void updateShopUserTrustScore(
            int shopId) {

        ShopDetails shop =
                shopRepo.findById(shopId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Shop not found"
                                ));

        Double averageRating =
                reviewRepo.calculateAverageRating(
                        shopId
                );

        if (averageRating == null) {
            averageRating = 0.0;
        }

        averageRating =
                Math.round(
                        averageRating * 100.0
                ) / 100.0;

        shop.setUserTrustScore(
                averageRating
        );

        shopRepo.save(shop);
    }

    // Prevents users from reviewing another user's order.
    private void validateOrderOwnership(
            CustomerOrder order,
            int userId) {

        if (order.getUser().getUser_id()
                != userId) {

            throw new ConflictException(
                    "This order does not belong to the user"
            );
        }
    }

    // Prevents users from editing or deleting another user's review.
    private void validateReviewOwnership(
            ShopReview review,
            int userId) {

        if (review.getUser().getUser_id()
                != userId) {

            throw new ConflictException(
                    "This review does not belong to the user"
            );
        }
    }

    // Converts blank comments to null and trims spaces.
    private String cleanComment(
            String comment) {

        if (comment == null ||
                comment.trim().isEmpty()) {

            return null;
        }

        return comment.trim();
    }

    // Removes null and duplicate keywords.
    private List<ReviewKeyword> cleanKeywords(
            List<ReviewKeyword> keywords) {

        if (keywords == null) {
            return new ArrayList<>();
        }

        List<ReviewKeyword> uniqueKeywords =
                new ArrayList<>();

        for (ReviewKeyword keyword : keywords) {

            if (keyword != null &&
                    !uniqueKeywords.contains(keyword)) {

                uniqueKeywords.add(keyword);
            }
        }

        return uniqueKeywords;
    }

    // Converts ShopReview entity into ReviewDTO.
    private ReviewDTO convertToDTO(
            ShopReview review) {

        String userName =
                review.getUser().getFname()
                + " "
                + review.getUser().getLname();

        return new ReviewDTO(
                review.getReviewId(),
                review.getOrder().getOrderId(),
                review.getShop().getShopId(),
                review.getShop().getShopName(),
                userName,
                review.getRating(),
                review.getComment(),
                review.getKeywords(),
                review.getReviewType(),
                review.getPointsAwarded(),
                review.getCreatedAt()
        );
    }
}