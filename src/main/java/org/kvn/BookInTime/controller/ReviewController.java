package org.kvn.BookInTime.controller;

import org.kvn.BookInTime.dto.request.ReviewRequestDTO;
import org.kvn.BookInTime.model.Review;
import org.kvn.BookInTime.model.Users;
import org.kvn.BookInTime.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController {
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    /**
     * API to add a review
     *
     * @param requestDTO The request object containing details from the client
     * @param user The registered user who is associated with this request, fetched from the session or context
     * @return a String response
     */
    @PostMapping("/addReview")
    public String addReview(@RequestBody ReviewRequestDTO requestDTO, @AuthenticationPrincipal Users user) {
        logger.info("addReview request received by : user {}: ReviewRequestDTO : {}", user.toString(), requestDTO.toString());

        return reviewService.addReview(requestDTO, user);
    }

    /**
     * API to get a review details
     *
     * @param id reviewId received from client
     * @return The review response associated with the provided reviewId
     */
    @GetMapping("/getReview")
    public Review getReview(@RequestParam("id") Integer id) {
        logger.info("getReview request received for review ID : {}", id);

        return reviewService.getReviewById(id);
    }

    /**
     * API to delete a review
     * @param reviewId The id of the review that is going to be deleted
     * @param user current loggedIn user
     * @return a string response
     */
    @DeleteMapping("/deleteReview")
    public String deleteReview(@RequestParam("id") Integer reviewId, @AuthenticationPrincipal Users user) {
        logger.info("deleteReview request received for review ID : {}, by user : {}", reviewId, user.toString());

        return reviewService.deleteReview(reviewId, user);
    }
}
