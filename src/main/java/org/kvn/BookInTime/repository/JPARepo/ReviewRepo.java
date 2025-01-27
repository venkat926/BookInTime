package org.kvn.BookInTime.repository.JPARepo;

import org.kvn.BookInTime.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review, Integer> {

    List<Review> findByMovieId(Integer movieId);

    List<Review> findByUserId(Integer userId);

    Review findReviewByMovieIdAndUserId(Integer movieId, Integer userId);
}
