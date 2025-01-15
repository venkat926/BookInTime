package org.kvn.BookInTime.repository;

import org.kvn.BookInTime.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowRepo extends JpaRepository<Show, Integer> {
    List<Show> findByTheaterId(Integer theaterId);

    List<Show> findByMovieId(Integer movieId);
}
