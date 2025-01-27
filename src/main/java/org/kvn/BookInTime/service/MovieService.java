package org.kvn.BookInTime.service;

import org.kvn.BookInTime.dto.request.MovieAdditionRequestDTO;
import org.kvn.BookInTime.dto.response.MovieResponseDTO;
import org.kvn.BookInTime.enums.Genre;
import org.kvn.BookInTime.enums.MovieFilter;
import org.kvn.BookInTime.exception.MovieException;
import org.kvn.BookInTime.model.Movie;
import org.kvn.BookInTime.model.Review;
import org.kvn.BookInTime.model.Show;
import org.kvn.BookInTime.repository.CacheRepo.MovieCacheRepo;
import org.kvn.BookInTime.repository.CacheRepo.ReviewCacheRepo;
import org.kvn.BookInTime.repository.CacheRepo.ShowCacheRepo;
import org.kvn.BookInTime.repository.JPARepo.MovieRepo;
import org.kvn.BookInTime.repository.JPARepo.ReviewRepo;
import org.kvn.BookInTime.repository.JPARepo.ShowRepo;
import org.kvn.BookInTime.service.movieFilter.MovieFilterFactory;
import org.kvn.BookInTime.service.movieFilter.MovieFilterStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    private MovieFilterFactory movieFilterFactory;
    @Autowired
    private ReviewRepo reviewRepo;
    @Autowired
    private ShowService showService;
    @Autowired
    private MovieCacheRepo movieCacheRepo;
    @Autowired
    private ReviewCacheRepo reviewCacheRepo;
    @Autowired
    private ShowCacheRepo showCacheRepo;
    @Autowired
    private ShowRepo showRepo;

    // add movie to DB
    public MovieResponseDTO addMovie(MovieAdditionRequestDTO requestDTO) {
        Movie movie = requestDTO.toMovie();

        // handle exception
        try {
            movie = movieRepo.save(movie);
            // adding movie to cache
            movieCacheRepo.saveMovie(movie, movie.getReviews(), movie.getShows());
        } catch (DataIntegrityViolationException e) {
            logger.error(e.getMessage());

            String message = e.getMessage();
            String errorMessage = "movie already exists [" +
                    message.substring(message.indexOf("Duplicate"), message.lastIndexOf("for")).trim() + "]";
            throw new DataIntegrityViolationException(errorMessage);
        }

        // return response
        return MovieResponseDTO.builder()
                .title(movie.getTitle())
                .genre(movie.getGenre())
                .rating(movie.getRating())
                .totalReviews(movie.getTotalReviews())
                .build();
    }

    // filter movies
    public List<MovieResponseDTO> filter(MovieFilter movieFilter, String value) {
        MovieFilterStrategy filterStrategy = movieFilterFactory.getFilterStrategy(movieFilter);
        List<Movie> movieList = filterStrategy.getFilteredMovies(value);
        return movieList.stream()
                .map(movie -> MovieResponseDTO.builder()
                        .title(movie.getTitle())
                        .genre(movie.getGenre())
                        .rating(movie.getRating())
                        .totalReviews(movie.getTotalReviews())
                        .build())
                .toList();
    }

    public List<Review> getAllReviews(Integer movieId) {

        // fetch movieReviewIDs from cache
        List<Integer> reviewIds = movieCacheRepo.getMovieReviewIds(movieId);
        if (reviewIds == null) {
            Movie movie = movieRepo.findById(movieId)
                    .orElseThrow(() -> new MovieException("Movie not found with the id [ " + movieId + " ]"));
            List<Review> reviews = movie.getReviews();
            movieCacheRepo.saveMovie(movie, reviews, movie.getShows());
            return reviews;
        } else {
            return reviewIds.stream()
                    .map(reviewId -> {
                        Review review = reviewCacheRepo.getReview(reviewId);
                        if (review == null) {
                            // Fetch review from DB if not found in cache
                            review = reviewRepo.findById(reviewId).orElse(null);

                            // Optionally add it to the cache for future use
                            if (review != null) {
                                reviewCacheRepo.saveReview(review);
                            }
                        }
                        return review;
                    })
                    .toList();
        }
    }

    public List<Show> getAllShows(Integer movieId) {
        // fetch all showIDs for this movie
        List<Integer> showIDs = movieCacheRepo.getMovieShowIds(movieId);
        if (showIDs == null) {
            List<Show> shows = showService.getAllShowsForMovie(movieId);
            if (shows != null)
                movieCacheRepo.saveMovieShows(movieId, shows);
            return shows;
        } else {
            return showIDs.stream()
                    .map(showId -> {
                        Show show = showCacheRepo.getShow(showId);
                        if (show == null) {
                            // fetch show from db if not found in cache
                            show = showRepo.findById(showId).orElse(null);
                            if (show != null) {
                                showCacheRepo.saveShow(show);
                            }
                        }
                        return show;
                    })
                    .toList();
        }
    }

    public List<MovieResponseDTO> getTop5MoviesByGenre(Genre genre) {

        List<Movie> movies = movieRepo.findTop5MoviesByGenre(genre);
        return movies.stream()
                .map(movie ->
                            MovieResponseDTO.builder()
                                    .title(movie.getTitle())
                                    .genre(movie.getGenre())
                                    .rating(movie.getRating())
                                    .totalReviews(movie.getTotalReviews())
                            .build()
                )
                .toList();
    }
}

