package org.kvn.BookInTime.service;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.dto.request.ShowCreationRequestDTO;
import org.kvn.BookInTime.dto.response.ShowCreationResponseDTO;
import org.kvn.BookInTime.enums.SeatType;
import org.kvn.BookInTime.exception.MovieException;
import org.kvn.BookInTime.exception.TheaterException;
import org.kvn.BookInTime.model.Movie;
import org.kvn.BookInTime.model.Seat;
import org.kvn.BookInTime.model.Show;
import org.kvn.BookInTime.model.Theater;
import org.kvn.BookInTime.repository.MovieRepo;
import org.kvn.BookInTime.repository.SeatRepo;
import org.kvn.BookInTime.repository.ShowRepo;
import org.kvn.BookInTime.repository.TheaterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ShowService {

    @Autowired
    private ShowRepo showRepo;

    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    private TheaterRepo theaterRepo;

    @Autowired
    private SeatRepo seatRepo;

    @Value("${seat.goldseat.price}")
    private Integer GOLD_SEAT_PRICE;

    @Value("${seat.regularseat.price}")
    private Integer REGULAR_SEAT_PRICE;


    public ShowCreationResponseDTO addShow(ShowCreationRequestDTO requestDTO) {
        // find movie by ID
        Movie movie = movieRepo.findById(requestDTO.getMovieId())
                .orElseThrow(() -> new MovieException("No such movie with id: " + requestDTO.getMovieId()));

        // find theater by ID
        Theater theater = theaterRepo.findById(requestDTO.getTheaterId())
                .orElseThrow(() -> new TheaterException("No such theater: " + requestDTO.getTheaterId()));

        // build show object to save
        Show show = requestDTO.toShow();
        show.setMovie(movie);
        show.setTheater(theater);
        List<Seat> seats = generateShowSeats(theater.getGoldSeatsCount(), theater.getRegularSeatsCount());
        show.setSeats(seats);
        showRepo.save(show);
        updateSeats(show, seats);

        // return response
        return ShowCreationResponseDTO.builder()
                .showDate(show.getShowDate())
                .showTiming(show.getShowTiming())
                .movieName(movie.getTitle())
                .theaterName(theater.getName())
                .creationDate(show.getCreatedOn())
                .build();
    }

    private void updateSeats(Show show, List<Seat> seats) {
        seats.forEach(seat -> seat.setShow(show));
        seatRepo.saveAll(seats);
    }

    // generate seats for a show
    private List<Seat> generateShowSeats( int goldSeatsCount, int regularSeatsCount) {
        List<Seat> seats = new ArrayList<>();
        for (int i=1; i<=goldSeatsCount; i++) {
            seats.add(Seat.builder()
                            .seatNo(i)
                            .seatType(SeatType.GOLD)
                            .isBooked(false)
                            .price(GOLD_SEAT_PRICE)
                    .build());
        }
        for (int i=1; i<=regularSeatsCount; i++) {
            seats.add(Seat.builder()
                            .seatNo(i)
                            .seatType(SeatType.REGULAR)
                            .isBooked(false)
                            .price(REGULAR_SEAT_PRICE)
                    .build());
        }
        return seatRepo.saveAll(seats);
    }

    public Show getShow(Integer showId) {
        return showRepo.findById(showId).orElse(null);
    }

    public List<Show> getAllShowsInTheater(Integer theaterId) {
        return showRepo.findByTheaterId(theaterId);
    }

    public List<Show> getAllShowsForMovie(Integer movieId) {
        return showRepo.findByMovieId(movieId);
    }
}
