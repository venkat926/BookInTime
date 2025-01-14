package org.kvn.BookInTime.controller;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.dto.request.TheaterCreationRequestDTO;
import org.kvn.BookInTime.dto.response.TheaterResponseDTO;
import org.kvn.BookInTime.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("theater")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @PostMapping("/addTheater")
    public ResponseEntity<TheaterResponseDTO> addTheater(@RequestBody TheaterCreationRequestDTO requestDTO) {
        log.info("received request to addTheater: {}", requestDTO.toString());

        return new ResponseEntity<>(theaterService.addTheater(requestDTO), HttpStatus.OK);
    }

    @GetMapping("/getTheater")
    public ResponseEntity<TheaterResponseDTO> getTheater(@RequestParam("id") Integer theaterId) {
        log.info("received request to getTheater: theaterId {}", theaterId);

        return new ResponseEntity<>(theaterService.getTheater(theaterId), HttpStatus.OK);
    }
}
