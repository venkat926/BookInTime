package org.kvn.BookInTime.service.movieFilter;

import org.kvn.BookInTime.enums.MovieFilter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MovieFilterFactory {

    private Map<MovieFilter, MovieFilterStrategy> filterStrategyMap = new HashMap<>();

    MovieFilterFactory(MovieTitleFilterImpl movieTitleFilterImpl, MovieGenreFilterImpl movieGenreFilterImpl) {
        filterStrategyMap.put(MovieFilter.TITLE, movieTitleFilterImpl);
        filterStrategyMap.put(MovieFilter.GENRE, movieGenreFilterImpl);
    }

    public MovieFilterStrategy getFilterStrategy(MovieFilter movieFilter) {
        return filterStrategyMap.get(movieFilter);
    }
}
