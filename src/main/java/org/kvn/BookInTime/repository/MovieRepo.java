package org.kvn.BookInTime.repository;

import org.kvn.BookInTime.enums.Genre;
import org.kvn.BookInTime.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MovieRepo extends JpaRepository<Movie, Integer> {

    List<Movie> findByTitleContaining(String value);

    List<Movie> findByGenre(Genre genre);
}
