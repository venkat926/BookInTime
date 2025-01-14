package org.kvn.BookInTime.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.kvn.BookInTime.enums.Genre;
import org.kvn.BookInTime.model.Movie;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieAdditionRequestDTO {
    @NotBlank(message = "Movie title should not be blank")
    private String title;

    private Genre genre;

    public Movie toMovie() {
        return Movie.builder()
                .title(title)
                .genre(genre)
                .rating(0.0)
                .build();
    }
}
