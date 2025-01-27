package org.kvn.BookInTime.service;

import jakarta.transaction.Transactional;
import org.kvn.BookInTime.dto.request.ReviewRequestDTO;
import org.kvn.BookInTime.exception.MovieException;
import org.kvn.BookInTime.exception.ReviewException;
import org.kvn.BookInTime.model.Movie;
import org.kvn.BookInTime.model.Review;
import org.kvn.BookInTime.model.Users;
import org.kvn.BookInTime.repository.JPARepo.MovieRepo;
import org.kvn.BookInTime.repository.JPARepo.ReviewRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ReviewService {

    private static Logger log = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private MovieRepo movieRepo;

    public static void setLog(Logger log) {
        ReviewService.log = log;
    }

    @Transactional
    public String addReview(ReviewRequestDTO requestDTO, Users user) {
        // find movie by ID
        Movie movie = movieRepo.findById(requestDTO.getMovieId())
                .orElseThrow(() -> new MovieException("Movie not found with ID: " + requestDTO.getMovieId()));

        // check the user already given a review to the same movie. if yes, delete the old review
        Review previousReview = reviewRepo.findReviewByMovieIdAndUserId(movie.getId(), user.getId());
        if (previousReview != null) {
           deleteReview(previousReview.getId(), user);
        }

        // create Review entity
        Review review = Review.builder()
                .rating(requestDTO.getRating())
                .movieReview(requestDTO.getMovieReview())
                .movie(movie)
                .user(user)
                .build();

        // save the review
        reviewRepo.save(review);

        // update movie's overall rating
        Double currentTotalRating = movie.getRating() * movie.getTotalReviews();
        int newTotalReviews = movie.getTotalReviews() + 1;
        Double newTotalRating = (currentTotalRating + review.getRating()) / newTotalReviews;

        movie.setTotalReviews(newTotalReviews);
        movie.setRating(newTotalRating);

        movieRepo.save(movie);

        // return response
        return "Review added successfully";
    }

    public Review getReviewById(Integer id) {
        return reviewRepo.findById(id).orElse(null);
    }

    @Transactional
    public String deleteReview(Integer reviewId, Users user) {
        // find review by ID
        Review review = reviewRepo.findById(reviewId).
                orElseThrow(() -> new ReviewException("Review not found with ID: " + reviewId));

        // check if the review is associated with the loggedIn user or not
        if (!Objects.equals(review.getUser().getId(), user.getId())) {
            throw new ReviewException("You do not have permission to delete this review :");
        }

        // find movie by id
        Movie movie = movieRepo.findById(reviewId).orElse(null);
        assert movie != null;

        // update move overall rating after deleting a rating
        if (movie.getTotalReviews() == 1) {
            movie.setTotalReviews(0);
            movie.setRating(0.0);
        } else {
            Double newTotalRating = movie.getRating() * movie.getTotalReviews() - review.getRating();
            Integer newTotalReviews = movie.getTotalReviews() - 1;
            movie.setTotalReviews(newTotalReviews);
            movie.setRating(newTotalRating/newTotalReviews);
        }
        movieRepo.save(movie);

        // delete the review from db
        reviewRepo.delete(review);

        // return response
        return "Review deleted successfully";

    }
}
