package org.kvn.BookInTime.repository.CacheRepo;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.model.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class SeatCacheRepo {

    private final String SEAT_KEY_PREFIX = "seat";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public void saveSeat(Seat seat) {
        log.info("saving seat to cache : {}", seat);
        redisTemplate.opsForValue().set(SEAT_KEY_PREFIX+seat.getId(), seat,10, TimeUnit.MINUTES);
    }

}
