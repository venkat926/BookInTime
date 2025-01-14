package org.kvn.BookInTime.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.kvn.BookInTime.enums.City;
import org.kvn.BookInTime.model.Theater;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TheaterCreationRequestDTO {

    @NotBlank(message = "Theater name is required")
    private String name;

    private String address;

    @NotNull(message = "City is required")
    private City city;

    @NotBlank(message = "regularSeatsCount is required")
    private int regularSeatsCount;

    @NotBlank(message = "goldSeatsCount is required")
    private int goldSeatsCount;

    public Theater toTheater() {
        return Theater.builder()
                .name(name)
                .address(address)
                .city(city)
                .regularSeatsCount(regularSeatsCount)
                .goldSeatsCount(goldSeatsCount)
                .build();
    }
}
