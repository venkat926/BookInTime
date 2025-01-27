package org.kvn.BookInTime.repository.cacheRepo;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.model.Movie;
import org.kvn.BookInTime.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class ReviewCacheRepo {

    private final String REVIEW_KEY_PREFIX = "review";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public void saveReview(Review review) {
        log.info("saving review to the cache : {}", review);
        String key = REVIEW_KEY_PREFIX + review.getId();
        redisTemplate.opsForValue().set(REVIEW_KEY_PREFIX + review.getId(), review, 10, TimeUnit.MINUTES);
        log.info("successfully saved review to the cache : {}", review);
    }

    public void saveReview(Review review, Movie movie) {
        log.info("saving review to the cache : {}", review);
        String key = REVIEW_KEY_PREFIX + review.getId();
        review.setMovie(movie);
        redisTemplate.opsForValue().set(REVIEW_KEY_PREFIX + review.getId(), review, 10, TimeUnit.MINUTES);
    }

    public Review getReview(Integer reviewId) {
        log.info("getting review from cache for id : {}", reviewId);
        String key = REVIEW_KEY_PREFIX + reviewId;
        return (Review) redisTemplate.opsForValue().get(key);
    }

    public void deleteReview(Integer reviewId) {
        log.info("deleting review from cache for id : {}", reviewId);
        String key = REVIEW_KEY_PREFIX + reviewId;
        redisTemplate.delete(key);
    }

}
