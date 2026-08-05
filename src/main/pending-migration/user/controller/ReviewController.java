package com.gutfriendly.app.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.user.dto.ReviewDTO;
import com.gutfriendly.app.user.dto.ReviewRequestDTO;
import com.gutfriendly.app.user.service.ReviewService;
import com.gutfriendly.app.user.dto.ReviewSummaryDTO;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    ///add 1 review for 1 order
    @PostMapping("/user/{userId}")
    public ResponseEntity<ReviewDTO> addReview(
            @PathVariable int userId,
            @RequestBody ReviewRequestDTO request) {

        return ResponseEntity.ok(
                reviewService.addReview(userId, request)
        );
    }

    // view all reviews
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByShop(
            @PathVariable int shopId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort;

        if (direction.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                reviewService.getReviewsByShop(
                        shopId,
                        pageable
                )
        );
    }
    
    
 // Returns average rating and star-wise review counts.
    @GetMapping("/shop/{shopId}/summary")
    public ResponseEntity<ReviewSummaryDTO>
            getReviewSummary(
                    @PathVariable int shopId) {

        return ResponseEntity.ok(
                reviewService.getReviewSummary(
                        shopId
                )
        );
    }
    

//    view 1 review
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> getReviewById(
            @PathVariable int reviewId) {

        return ResponseEntity.ok(
                reviewService.getReviewById(reviewId)
        );
    }

//      Edit user's own review.
//      Reward points remain unchanged.
     
    @PutMapping("/user/{userId}/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable int userId,
            @PathVariable int reviewId,
            @RequestBody ReviewRequestDTO request) {

        return ResponseEntity.ok(
                reviewService.updateReview(
                        userId,
                        reviewId,
                        request
                )
        );
    }

    
//      Delete user's own review.
//      Reward points remain unchanged.
      
    @DeleteMapping("/user/{userId}/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable int userId,
            @PathVariable int reviewId) {

        reviewService.deleteReview(userId, reviewId);

        return ResponseEntity.ok(
                "Review deleted successfully"
        );
    }
}