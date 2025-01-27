package org.kvn.BookInTime.repository.CacheRepo;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.model.Seat;
import org.kvn.BookInTime.model.Show;
import org.kvn.BookInTime.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class ShowCacheRepo {

    private final String SHOW_KEY_PREFIX = "show";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private TicketCacheRepo ticketCacheRepo;
    @Autowired
    private SeatCacheRepo seatCacheRepo;

    public void saveShow(Show show) {
        log.info("saving show to cache : {}", show);
        redisTemplate.opsForValue().set(SHOW_KEY_PREFIX+show.getId(), show, 10, TimeUnit.MINUTES);
    }

    public void saveShow(Show show, List<Ticket> bookedTickets, List<Seat> seats) {
        log.info("saving show to cache : {}", show);

        // save show to the cache
        redisTemplate.opsForValue().set(SHOW_KEY_PREFIX+show.getId(), show, 10, TimeUnit.MINUTES);


        if (bookedTickets == null) bookedTickets = new ArrayList<>();
        // save bookedTicketIDs to cache
        List<Integer> bookedTicketIds = bookedTickets.stream().map(Ticket::getId).toList();
        redisTemplate.opsForValue().set(SHOW_KEY_PREFIX+show.getId()+":tickets", show, 10, TimeUnit.MINUTES);

        // save booked ticket separately
        for (Ticket ticket : bookedTickets) {
            ticketCacheRepo.saveTicket(ticket, ticket.getBookedSeats());
        }

        if (seats == null) seats = new ArrayList<>();
        // save showSeatIDs to cache
        List<Integer> seatIds = seats.stream().map(Seat::getId).toList();
        redisTemplate.opsForValue().set(SHOW_KEY_PREFIX+show.getId()+":seats", show, 10, TimeUnit.MINUTES);

        // save seats separately
        for (Seat seat : seats) {
            seatCacheRepo.saveSeat(seat);
        }
    }

    public Show getShow(Integer showId) {
        log.info("getting show from cache : {}", showId);
        return (Show) redisTemplate.opsForValue().get(SHOW_KEY_PREFIX+showId);
    }
}
