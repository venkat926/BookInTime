package org.kvn.BookInTime.controller;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.dto.request.ShowCreationRequestDTO;
import org.kvn.BookInTime.dto.response.ShowCreationResponseDTO;
import org.kvn.BookInTime.model.Show;
import org.kvn.BookInTime.repository.ShowRepo;
import org.kvn.BookInTime.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/show")
public class ShowController {

    @Autowired
    private ShowService showService;

    /**
     * API to add a show
     */
    @PostMapping("/addShow")
    public ResponseEntity<ShowCreationResponseDTO> addShow(@RequestBody ShowCreationRequestDTO requestDTO) {
        log.info("received a request to add a Show: ShowCreationRequestDTO:{}", requestDTO.toString());

        return new ResponseEntity<>(showService.addShow(requestDTO), HttpStatus.OK);
    }

    /**
     * API to get the show details
     */
    @GetMapping("/getShow")
    public Show getShow(@RequestParam("id") Integer showId) {
        log.info("received a request to get a Show: ShowId:{}", showId);

        return showService.getShow(showId);
    }
}
