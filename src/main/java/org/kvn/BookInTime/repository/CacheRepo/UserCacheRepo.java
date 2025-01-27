package org.kvn.BookInTime.repository.CacheRepo;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.model.Review;
import org.kvn.BookInTime.model.Ticket;
import org.kvn.BookInTime.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class UserCacheRepo {

    private final String USER_KEY_PREFIX = "user";

    @Value("${redis.user.details.timeout}")
    private int timeout;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private TicketCacheRepo ticketCacheRepo;

    @Autowired
    private ReviewCacheRepo reviewCacheRepo;

    public void saveUser(Users user, List<Ticket> tickets, List<Review> reviews) {
        log.info("saving user to Redis cache user {}", user.toString());

        // save user profile
        redisTemplate.opsForValue().set(USER_KEY_PREFIX + user.getId(), user, 10, TimeUnit.MINUTES);

        if (tickets == null) {tickets = new ArrayList<>();}
        // save ticket IDs separately
        List<Integer> ticketIds = tickets.stream().map(Ticket::getId).toList();
        redisTemplate.opsForSet().add(USER_KEY_PREFIX + user.getId() + ":tickets", ticketIds, 10, TimeUnit.MINUTES);

        // save each ticket separately
        for (Ticket ticket : tickets) {
            ticketCacheRepo.saveTicket(ticket, ticket.getBookedSeats());
        }

        if (reviews == null) {reviews = new ArrayList<>();}
        // save review IDs separately
        List<Integer> reviewIds = reviews.stream().map(Review::getId).toList();
        redisTemplate.opsForSet().add(USER_KEY_PREFIX + user.getId() + ":reviews", reviewIds, 10, TimeUnit.MINUTES);

        // save each review separately
        for (Review review : reviews) {
            reviewCacheRepo.saveReview(review);
        }
    }

    public Users getUser(int id) {
        log.info("checking the cache for user with id {}", id);
        String key = USER_KEY_PREFIX + String.valueOf(id);
        return (Users) redisTemplate.opsForValue().get(key);
    }

    public void removeUser(int id) {
        String key = USER_KEY_PREFIX + String.valueOf(id);
        redisTemplate.expire(key, 1, TimeUnit.MILLISECONDS);
    }

}
