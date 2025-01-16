package org.kvn.BookInTime.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.kvn.BookInTime.dto.request.MovieAdditionRequestDTO;
import org.kvn.BookInTime.dto.response.MovieResponseDTO;
import org.kvn.BookInTime.enums.Genre;
import org.kvn.BookInTime.enums.MovieFilter;
import org.kvn.BookInTime.model.Review;
import org.kvn.BookInTime.model.Show;
import org.kvn.BookInTime.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieService movieService;

    /**
     * Add Movie API
     */
    @PostMapping("/addMovie")
    public ResponseEntity<MovieResponseDTO> addMovie(@RequestBody MovieAdditionRequestDTO requestDTO){
        logger.info("Received request to add movie");
        logger.info("addMovie movie={}", requestDTO);

        MovieResponseDTO responseDTO = movieService.addMovie(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    /**
     * Filter movies with movie name or genre API
     */
    @GetMapping("/filter")
    public ResponseEntity<List<MovieResponseDTO>> filterMovie(
            @NotNull(message = "filterBy must not be null") @RequestParam("filterBy") MovieFilter movieFilter,
            @NotBlank(message = "value must not be blank") @RequestParam("value") String value
            ){
        logger.info("Received request to filter movie");
        logger.info("filterBy={}, with value={}", movieFilter, value);

        List<MovieResponseDTO> movieResponseDTOList = movieService.filter(movieFilter, value);
        return new ResponseEntity<>(movieResponseDTOList, HttpStatus.OK);
    }

    /**
     * get all reviews associated with movie
     */
    @GetMapping("/getAllReviews")
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam("id") Integer movieId) {
        logger.info("Received request to get all reviews");

        List<Review> reviews = movieService.getAllReviews(movieId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * API to get all the shows for a movie
     */
    @GetMapping("/gatAllShows")
    public ResponseEntity<List<Show>> getAllShows(@RequestParam("id") Integer movieId) {
        logger.info("Received request to get all shows for movie with id={}", movieId);

        return new ResponseEntity<>(movieService.getAllShows(movieId), HttpStatus.OK);
    }

    /**
     * API to get top 5 movies by genre
     */
    @GetMapping("/top5ByGenre")
    public ResponseEntity<List<MovieResponseDTO>> getTop5ByGenre(@RequestParam("genre") Genre genre) {
        logger.info("Received request to get top 5 Movies for genre={}", genre);

        return new ResponseEntity<>(movieService.getTop5MoviesByGenre(genre), HttpStatus.OK);
    }

}
