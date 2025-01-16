package org.kvn.BookInTime.controller;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.dto.request.TicketBookingRequestDTO;
import org.kvn.BookInTime.dto.response.TicketBookingResponseDTO;
import org.kvn.BookInTime.model.Ticket;
import org.kvn.BookInTime.model.Users;
import org.kvn.BookInTime.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    /**
     * API to book tickets
     */
    @PostMapping("/bookTickets")
    public ResponseEntity<TicketBookingResponseDTO> bookTickets(@RequestBody TicketBookingRequestDTO requestDTO, @AuthenticationPrincipal Users user) {
        log.info("received request to bookTickets: {}", requestDTO.toString());

        return new ResponseEntity<>(ticketService.bookTickets(requestDTO, user), HttpStatus.OK);
    }

    /**
     * API to get ticket details
     */
    @GetMapping("/id")
    public Ticket getTicketById(@RequestParam("id") Integer ticketId) {
        log.info("received request to getTicket for ticketId: {}", ticketId);

        return ticketService.getTicketById(ticketId);
    }

    /**
     * API to cancel a confirmed ticket
     */
    @PostMapping("/cancelTicket")
    public Ticket cancelTicket(@RequestParam("id") Integer ticketId) {
        log.info("received request to cancelTicket for ticketId: {}", ticketId);

        return ticketService.cancelTicket(ticketId);
    }


}
