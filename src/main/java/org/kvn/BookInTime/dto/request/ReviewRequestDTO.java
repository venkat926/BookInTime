package org.kvn.BookInTime.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDTO {

    @NotNull(message = "rating cannot be null")
    @DecimalMin(value = "0", inclusive = false, message = "Rating must be greater than 0")
    @DecimalMax(value = "5", inclusive = true, message = "rating must not exceed 5")
    private Double rating;

    private String movieReview;

    @NotNull(message = "Movie ID is required")
    private Integer movieId;
}
