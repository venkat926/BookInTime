package org.kvn.BookInTime.repository.CacheRepo;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.model.Movie;
import org.kvn.BookInTime.model.Review;
import org.kvn.BookInTime.model.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class MovieCacheRepo {

    private final String MOVIE_KEY_PREFIX = "movie";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ReviewCacheRepo reviewCacheRepo;

    @Autowired
    private ShowCacheRepo showCacheRepo;

    public void saveMovie(Movie movie, List<Review> reviews, List<Show> shows){
        log.info("saving Movie to cache: saveMovie {}", movie);

        // save movie to cache
        redisTemplate.opsForValue().set(MOVIE_KEY_PREFIX+movie.getId(), movie, 10, TimeUnit.MINUTES);

       saveMovieReviews(movie.getId(), reviews);

       saveMovieShows(movie.getId(), shows);

    }

    public void saveMovieShows(Integer movieId, List<Show> shows) {
        log.info("saving movieShows to cache: movieId={}", movieId);
        // save show IDs to cache
        if (shows == null) shows = new ArrayList<>();
        List<Integer> showIds = shows.stream().map(Show::getId).toList();
        redisTemplate.opsForValue().set(MOVIE_KEY_PREFIX+movieId+":shows", showIds, 10, TimeUnit.MINUTES);

        // save shows separately
        for (Show show : shows) {
            showCacheRepo.saveShow(show, show.getBookedTickets(), show.getSeats());
        }
    }

    public void saveMovieReviews(Integer movieId, List<Review> reviews) {
        log.info("saving movieReviews to cache: movieId={}", movieId);
        // save review IDs to cache
        if (reviews == null) reviews = new ArrayList<>();
        List<Integer> reviewIds = reviews.stream().map(Review::getId).toList();
        redisTemplate.opsForValue().set(MOVIE_KEY_PREFIX+movieId+":reviews", reviewIds, 10, TimeUnit.MINUTES);

        // save review IDs separately
        for (Review review : reviews) {
            reviewCacheRepo.saveReview(review);
        }
    }

    public Movie getMovie(Integer movieId){
        log.info("get movie from cache: movieId={}", movieId);
        return (Movie) redisTemplate.opsForValue().get(MOVIE_KEY_PREFIX+movieId);
    }

    public List<Integer> getMovieReviewIds(Integer movieId) {
        log.info("get movieReviewIds from cache: movieId={}", movieId);
        return (List<Integer>) redisTemplate.opsForValue().get(MOVIE_KEY_PREFIX+movieId+":reviews");
    }

    public List<Integer> getMovieShowIds(Integer movieId) {
        log.info("get movieShowIds from cache: movieId={}", movieId);
        return (List<Integer>) redisTemplate.opsForValue().get(MOVIE_KEY_PREFIX+movieId+":shows");
    }


}
