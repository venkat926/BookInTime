package org.kvn.BookInTime.repository.JPARepo;

import org.kvn.BookInTime.enums.City;
import org.kvn.BookInTime.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShowRepo extends JpaRepository<Show, Integer> {
    List<Show> findByTheaterId(Integer theaterId);

    List<Show> findByMovieId(Integer movieId);

    @Query("select s from Show s join s.theater t where t.city = :city")
    List<Show> findByShowsByCity(City city);
}
