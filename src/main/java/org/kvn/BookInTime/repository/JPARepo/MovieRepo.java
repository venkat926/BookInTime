package org.kvn.BookInTime.repository.JPARepo;

import org.kvn.BookInTime.enums.Genre;
import org.kvn.BookInTime.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MovieRepo extends JpaRepository<Movie, Integer> {

    List<Movie> findByTitleContaining(String value);

    List<Movie> findByGenre(Genre genre);

    @Query("select m from Movie m where m.genre = :genre order by m.rating desc ")
    List<Movie> findTop5MoviesByGenre(Genre genre);
}
