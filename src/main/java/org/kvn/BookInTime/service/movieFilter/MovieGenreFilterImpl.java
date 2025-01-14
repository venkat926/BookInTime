package org.kvn.BookInTime.service.movieFilter;

import org.kvn.BookInTime.enums.Genre;
import org.kvn.BookInTime.model.Movie;
import org.kvn.BookInTime.repository.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieGenreFilterImpl implements MovieFilterStrategy{

    @Autowired
    private MovieRepo movieRepo;

    @Override
    public List<Movie> getFilteredMovies(String value) {
        return movieRepo.findByGenre(Genre.valueOf(value));
    }
}
