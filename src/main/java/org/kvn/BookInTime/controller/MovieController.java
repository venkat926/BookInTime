package org.kvn.BookInTime.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.kvn.BookInTime.dto.request.MovieAdditionRequestDTO;
import org.kvn.BookInTime.dto.response.MovieResponseDTO;
import org.kvn.BookInTime.enums.MovieFilter;
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

}
