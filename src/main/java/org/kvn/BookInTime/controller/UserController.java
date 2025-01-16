package org.kvn.BookInTime.controller;

import org.kvn.BookInTime.dto.request.UserCreationRequestDTO;
import org.kvn.BookInTime.dto.response.UserCreationResponseDTO;
import org.kvn.BookInTime.model.Review;
import org.kvn.BookInTime.model.Ticket;
import org.kvn.BookInTime.model.Users;
import org.kvn.BookInTime.service.UserService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Add User API
     */
    @PostMapping("/addUser")
    public ResponseEntity<UserCreationResponseDTO> addUser(@RequestBody @Validated UserCreationRequestDTO requestDTO) {
        logger.info("AddUser request received. Adding the new User to the DB");

        UserCreationResponseDTO responseDTO = userService.addUser(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    /**
     * Add Admin API
     */
    @PostMapping("/addAdmin")
    public ResponseEntity<UserCreationResponseDTO> addAdmin(@RequestBody @Validated UserCreationRequestDTO requestDTO) {
        logger.info("AddAdmin request received. Adding Admin to the DB");

        UserCreationResponseDTO responseDTO = userService.addAdmin(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    /**
     * API to get all the reviews given a specific user
     */
    @GetMapping("/getAllReviews")
    public ResponseEntity<List<Review>> getAllReviews(@AuthenticationPrincipal Users user) {
        logger.info("GetAllReviews request received by user: {}. Getting all reviews from DB", user.toString());

        return new ResponseEntity<>(userService.getAllReviewsById(user.getId()), HttpStatus.OK);
    }

    /**
     * API to get all the ticket details booked by a specific user
     */
    @GetMapping("/getAllTickets")
    public List<Ticket> getAllTickets(@AuthenticationPrincipal Users user) {
        logger.info("received request to getAllTickets for user: {}", user.toString());

        return userService.getAllTicketsForUser(user);
    }
}
