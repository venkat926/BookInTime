package org.kvn.BookInTime.repository.cacheRepo;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.model.Seat;
import org.kvn.BookInTime.model.Show;
import org.kvn.BookInTime.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class TicketCacheRepo {

    private final String TICKET_KEY_PREFIX = "ticket";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveTicket(Ticket ticket) {
        log.info("saving ticket to cache : {}", ticket);
        // save ticket to cache
        redisTemplate.opsForValue().set(TICKET_KEY_PREFIX + ticket.getId(), ticket);
    }

    public void saveTicket(Ticket ticket, Show show, List<Seat> seats) {
        log.info("saving ticket to cache : {}", ticket);
        // save ticket to cache
        ticket.setShow(show);
        ticket.setBookedSeats(seats);
        redisTemplate.opsForValue().set(TICKET_KEY_PREFIX + ticket.getId(), ticket);
    }

    public Ticket getTicket(Integer ticketId) {
        log.info("getting ticket from cache : {}", ticketId);
        return (Ticket) redisTemplate.opsForValue().get(TICKET_KEY_PREFIX + ticketId);
    }
}
