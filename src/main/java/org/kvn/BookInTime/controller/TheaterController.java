package org.kvn.BookInTime.controller;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.dto.request.TheaterCreationRequestDTO;
import org.kvn.BookInTime.dto.response.TheaterResponseDTO;
import org.kvn.BookInTime.model.Show;
import org.kvn.BookInTime.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("theater")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    /**
     * API to add theater
     *
     * @param requestDTO is a request info from client to add theater
     * @return response
     */
    @PostMapping("/addTheater")
    public ResponseEntity<TheaterResponseDTO> addTheater(@RequestBody TheaterCreationRequestDTO requestDTO) {
        log.info("received request to addTheater: {}", requestDTO.toString());

        return new ResponseEntity<>(theaterService.addTheater(requestDTO), HttpStatus.OK);
    }

    /**
     * API to get theater information
     *
     * @param theaterId is the id of the theater to which info is requested
     * @return theater info
     */
    @GetMapping("/getTheater")
    public ResponseEntity<TheaterResponseDTO> getTheater(@RequestParam("id") Integer theaterId) {
        log.info("received request to getTheater: theaterId {}", theaterId);

        return new ResponseEntity<>(theaterService.getTheater(theaterId), HttpStatus.OK);
    }

    /**
     * API to get all the shows running in a theater
     *
     * @param theaterId id of the theater
     * @return list of shows
     */
    @GetMapping("/getAllShows")
    public ResponseEntity<List<Show>> getAllShows(@RequestParam("id") Integer theaterId) {
        log.info("received request to getAllShows for theater with: theaterId {}", theaterId);

        return new ResponseEntity<>(theaterService.getAllShows(theaterId), HttpStatus.OK);
    }
}
