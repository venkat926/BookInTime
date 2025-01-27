package org.kvn.BookInTime.repository.cacheRepo;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.model.Theater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TheaterCacheRepo {

    private final String THEATER_KEY_PREFIX = "theater";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public void saveTheater(Theater theater) {
        log.info("saving theater to cache: saveTheater({})", theater);
        // save theater to cache
        String key = THEATER_KEY_PREFIX + theater.getId();
        redisTemplate.opsForValue().set(key, theater);
    }

    public Theater getTheater(Integer theaterId) {
        log.info("getting theater from cache: getTheater {}", theaterId);
        // get theater form cache
        String key = THEATER_KEY_PREFIX + theaterId;
        return (Theater) redisTemplate.opsForValue().get(key);
    }

}
