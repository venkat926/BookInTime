package org.kvn.BookInTime.service;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.dto.request.TheaterCreationRequestDTO;
import org.kvn.BookInTime.dto.response.TheaterResponseDTO;
import org.kvn.BookInTime.exception.TheaterException;
import org.kvn.BookInTime.model.Theater;
import org.kvn.BookInTime.repository.TheaterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TheaterService {

    @Autowired
    private TheaterRepo theaterRepo;

    public TheaterResponseDTO addTheater(TheaterCreationRequestDTO requestDTO) {
        Theater theater = requestDTO.toTheater();

        // handle exception
        try {
            theaterRepo.save(theater);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());

            String message = e.getMessage();
            String errorMessage = "Theater name already exists [" +
                    message.substring(message.indexOf("Duplicate"), message.lastIndexOf("for")).trim() + "]";
            throw new DataIntegrityViolationException(errorMessage);
        }

        // return response
        return TheaterResponseDTO.builder()
                .name(theater.getName())
                .address(theater.getAddress())
                .city(theater.getCity())
                .regularSeatsCount(theater.getRegularSeatsCount())
                .goldSeatsCount(theater.getGoldSeatsCount())
                .build();
    }

    public TheaterResponseDTO getTheater(Integer theaterId) {
        Theater theater = theaterRepo.findById(theaterId)
                .orElseThrow(() -> new TheaterException("No theater exists with id [" + theaterId + "]"));

        return TheaterResponseDTO.builder()
                .name(theater.getName())
                .address(theater.getAddress())
                .city(theater.getCity())
                .regularSeatsCount(theater.getRegularSeatsCount())
                .goldSeatsCount(theater.getGoldSeatsCount())
                .build();
    }
}
