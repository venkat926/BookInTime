package org.kvn.BookInTime.dto.response;

import lombok.*;
import org.kvn.BookInTime.enums.Genre;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieResponseDTO {
    private String title;

    private Genre genre;

    private Double rating;

    private Integer totalReviews;
}
