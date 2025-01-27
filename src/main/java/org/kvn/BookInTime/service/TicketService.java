package org.kvn.BookInTime.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.consumer.TicketBookingNotification;
import org.kvn.BookInTime.dto.request.TicketBookingRequestDTO;
import org.kvn.BookInTime.dto.response.TicketBookingResponseDTO;
import org.kvn.BookInTime.enums.SeatType;
import org.kvn.BookInTime.enums.TicketStatus;
import org.kvn.BookInTime.exception.ShowException;
import org.kvn.BookInTime.exception.TicketException;
import org.kvn.BookInTime.model.Seat;
import org.kvn.BookInTime.model.Show;
import org.kvn.BookInTime.model.Ticket;
import org.kvn.BookInTime.model.Users;
import org.kvn.BookInTime.repository.CacheRepo.UserCacheRepo;
import org.kvn.BookInTime.repository.JPARepo.SeatRepo;
import org.kvn.BookInTime.repository.JPARepo.ShowRepo;
import org.kvn.BookInTime.repository.JPARepo.TicketRepo;
import org.kvn.BookInTime.repository.JPARepo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class TicketService {

    @Autowired
    private TicketRepo ticketRepo;
    @Autowired
    private ShowRepo showRepo;
    @Autowired
    private SeatRepo seatRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TicketBookingNotification ticketBookingNotification;
    @Autowired
    private UserCacheRepo userCacheRepo;

    @Transactional
    public TicketBookingResponseDTO bookTickets(TicketBookingRequestDTO requestDTO, Users user) {
        // find show by id
        Show show = showRepo.findById(requestDTO.getShowId())
                .orElseThrow(() -> new ShowException("No such show with id: " + requestDTO.getShowId()));

        // check the requested seats are already booked or not
        requestDTO.getSeatNumbers().forEach(seat -> {

            SeatType seatType = seat.charAt(0)=='G' ? SeatType.GOLD : (seat.charAt(0)=='R' ? SeatType.REGULAR: SeatType.valueOf("none"));
            Integer seatNumber = Integer.parseInt(seat.substring(1));
            Seat showSeat = seatRepo.findByShowIdAndSeatTypeAndSeatNo(show.getId(), seatType, seatNumber);
            if (showSeat.isBooked()) {
                throw new TicketException("Seat [ " + seat + " ] is already booked");
            }
        });

        // create a ticket for this show, user and save it
        Ticket ticket = Ticket.builder()
                .show(show)
                .user(user)
                .ticketStatus(TicketStatus.BOOKED) // TODO: update the schema and change it to IN_TRANSIT
                .build();
        ticketRepo.save(ticket);

        //
        AtomicReference<Integer> amount = new AtomicReference<>(0);
        List<Seat> bookedSeats = new ArrayList<>();
        requestDTO.getSeatNumbers().forEach(seat -> {
            SeatType seatType = seat.charAt(0)=='G' ? SeatType.GOLD : SeatType.REGULAR;
            Integer seatNumber = Integer.parseInt(seat.substring(1));

            Seat showSeat = seatRepo.findByShowIdAndSeatTypeAndSeatNo(show.getId(), seatType, seatNumber);
            showSeat.setBooked(true);
            showSeat.setTicket(ticket);
            bookedSeats.add(showSeat);
            amount.updateAndGet(v -> v + showSeat.getPrice());
        });

        ticket.setTicketStatus(TicketStatus.BOOKED);
        ticket.setAmount(amount.getOpaque().doubleValue());
        ticket.setBookedSeats(bookedSeats);
        ticketRepo.save(ticket);
        seatRepo.saveAll(bookedSeats);
        // get user
        Integer userId = user.getId();
        user = userCacheRepo.getUser(userId);
        if (user == null) {
            user = userRepo.findById(userId).orElse(null);
        } else {
            log.info("user details has been fetched from Redis cache ; {}", user);

        }

        List<Ticket> userTickets = user.getBookedTickets();
        userTickets.add(ticket);
        user.setBookedTickets(userTickets);
        userRepo.save(user);
        user.getBookedTickets().size();
        user.getReviews().size();
        userCacheRepo.setUser(user.getId(), user);

        TicketBookingResponseDTO responseDTO = TicketBookingResponseDTO.builder()
                .ticketStatus(ticket.getTicketStatus())
                .ticketPrice(ticket.getAmount())
                .showdate(show.getShowDate())
                .showTiming(show.getShowTiming())
                .theaterName(show.getTheater().getName())
                .userName(user.getName())
                .bookedSeats(bookedSeats)
                .bookedAt(ticket.getBookingTime())
                .build();

        // send email notification
        ticketBookingNotification.sendTicketBookedNotification(ticket);

        // return response
        return responseDTO;


    }

    public Ticket getTicketById(Integer ticketId) {
        return ticketRepo.findById(ticketId)
                .orElseThrow(() -> new TicketException("No such ticket with id: " + ticketId));
    }

    @Transactional
    public Ticket cancelTicket(Integer ticketId) {
        // find ticket by ID
        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new TicketException("No such ticket with id: " + ticketId));

        // update ticket status to CANCELLED
        ticket.setTicketStatus(TicketStatus.CANCELLED);
        ticketRepo.save(ticket);

        // update seats status to booked false
        List<Seat> bookedSeats = ticket.getBookedSeats();
        bookedSeats.forEach(seat -> {
            seat.setBooked(false);
            seat.setTicket(null);
        });
        ticket.setBookedSeats(null);
        seatRepo.saveAll(bookedSeats);

        // send email notification
        ticketBookingNotification.sendTicketCancelledNotification(ticket);
        // return response
        return ticket;
    }
}
