package org.kvn.BookInTime.repository.CacheRepo;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.model.Show;
import org.kvn.BookInTime.model.Theater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class TheaterCacheRepo {

    private final String THEATER_KEY_PREFIX = "theater";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ShowCacheRepo showCacheRepo;

    public void saveTheater(Theater theater, List<Show> shows) {
        log.info("saving theater to cache: saveTheater({})", theater);
        // save theater to cache
        String key = THEATER_KEY_PREFIX + theater.getId();
        redisTemplate.opsForValue().set(key, theater);

        // save showIDs to cache
        if (shows == null) shows = new ArrayList<>();
        List<Integer> showIds = shows.stream().map(Show::getId).toList();
        redisTemplate.opsForValue().set(THEATER_KEY_PREFIX + theater.getId() + ":shows", theater, 10, TimeUnit.MINUTES);

        // save shows separately
        for (Show show : shows) {
            showCacheRepo.saveShow(show, show.getBookedTickets(), show.getSeats());
        }
    }
    public Theater getTheater(String theaterId) {
        log.info("getting theater from cache: getTheater {}", theaterId);
        String key = THEATER_KEY_PREFIX + theaterId;
        return (Theater) redisTemplate.opsForValue().get(key);
    }
    public void removeTheater(String theaterId) {
        log.info("removing theater from cache: deleteTheater {}", theaterId);
        String key = THEATER_KEY_PREFIX + theaterId;
        redisTemplate.delete(key);
    }
}
