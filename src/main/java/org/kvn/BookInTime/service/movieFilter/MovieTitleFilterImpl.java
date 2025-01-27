package org.kvn.BookInTime.service.movieFilter;

import org.kvn.BookInTime.model.Movie;
import org.kvn.BookInTime.repository.JPARepo.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieTitleFilterImpl implements MovieFilterStrategy {

    @Autowired
    private MovieRepo movieRepo;

    @Override
    public List<Movie> getFilteredMovies(String value) {
        return movieRepo.findByTitleContaining(value);
    }
}
