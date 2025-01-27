package org.kvn.BookInTime.repository.cacheRepo;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.model.Movie;
import org.kvn.BookInTime.model.Show;
import org.kvn.BookInTime.model.Theater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class ShowCacheRepo {

    private final String SHOW_KEY_PREFIX = "show";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public void saveShow(Show show) {
        log.info("saving show to cache : {}", show);
        redisTemplate.opsForValue().set(SHOW_KEY_PREFIX+show.getId(), show, 10, TimeUnit.MINUTES);
    }

    public void saveShow(Show show, Movie movie, Theater theater) {
        log.info("saving show to cache : {}", show);
        show.setMovie(movie);
        show.setTheater(theater);
        redisTemplate.opsForValue().set(SHOW_KEY_PREFIX+show.getId(), show, 10, TimeUnit.MINUTES);
    }

    public Show getShow(Integer showId) {
        log.info("getting show from cache : {}", showId);
        return (Show) redisTemplate.opsForValue().get(SHOW_KEY_PREFIX+showId);
    }


}
