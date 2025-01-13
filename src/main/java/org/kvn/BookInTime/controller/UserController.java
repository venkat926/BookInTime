package org.kvn.BookInTime.controller;

import org.kvn.BookInTime.dto.request.UserCreationRequestDTO;
import org.kvn.BookInTime.dto.response.UserCreationResponseDTO;
import org.kvn.BookInTime.service.UserService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
