package org.kvn.BookInTime.service;

import org.kvn.BookInTime.dto.request.MovieAdditionRequestDTO;
import org.kvn.BookInTime.dto.response.MovieResponseDTO;
import org.kvn.BookInTime.enums.MovieFilter;
import org.kvn.BookInTime.model.Movie;
import org.kvn.BookInTime.repository.MovieRepo;
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

    // add movie to DB
    public MovieResponseDTO addMovie(MovieAdditionRequestDTO requestDTO) {
        Movie movie = requestDTO.toMovie();

        // handle exception
        try {
            movie = movieRepo.save(movie);
        } catch (DataIntegrityViolationException e) {
            logger.error(e.getMessage());

            String message = e.getMessage();
            String errorMessage = "movie already exists [" +
                    message.substring(message.indexOf("Duplicate"), message.lastIndexOf("for")).trim() + "]";
            throw new DataIntegrityViolationException(errorMessage);
        }

        // return response
        if (movie == null) return null;
        return MovieResponseDTO.builder()
                .title(movie.getTitle())
                .genre(movie.getGenre())
                .rating(movie.getRating())
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
                        .build())
                .toList();
    }
}

