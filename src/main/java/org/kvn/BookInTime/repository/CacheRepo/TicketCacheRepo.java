package org.kvn.BookInTime.repository.CacheRepo;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.model.Seat;
import org.kvn.BookInTime.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class TicketCacheRepo {

    private final String TICKET_KEY_PREFIX = "ticket";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SeatCacheRepo seatCacheRepo;

    public void saveTicket(Ticket ticket, List<Seat> seats) {
        log.info("saving ticket to cache : {}", ticket);
        // save ticket to cache
        redisTemplate.opsForValue().set(TICKET_KEY_PREFIX + ticket.getId(), ticket);

       // save seatIDs to cache
        if (seats == null) seats = new ArrayList<>();
        List<Integer> seatIds = seats.stream().map(Seat::getId).toList();
        redisTemplate.opsForValue().set(TICKET_KEY_PREFIX + ticket.getId() + ":seats", ticket, 10, TimeUnit.MINUTES);

        // save seats separately
        for (Seat seat : seats) {
            seatCacheRepo.saveSeat(seat);
        }
    }

    public Ticket getTicket(String ticketId) {
        log.info("getting ticket from cache : {}", ticketId);
        return (Ticket) redisTemplate.opsForValue().get(TICKET_KEY_PREFIX + ticketId);
    }

    public void removeTicket(String ticketId) {
        log.info("removing ticket from cache : {}", ticketId);
        redisTemplate.delete(TICKET_KEY_PREFIX + ticketId);
    }

}
