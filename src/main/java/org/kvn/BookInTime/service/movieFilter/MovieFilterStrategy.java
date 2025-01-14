package org.kvn.BookInTime.service.movieFilter;

import org.kvn.BookInTime.model.Movie;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MovieFilterStrategy {
    public List<Movie> getFilteredMovies(String value);
}
