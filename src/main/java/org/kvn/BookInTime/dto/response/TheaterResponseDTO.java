package org.kvn.BookInTime.dto.response;

import lombok.*;
import org.kvn.BookInTime.enums.City;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TheaterResponseDTO {

    private String name;

    private String address;

    private City city;

    private int regularSeatsCount;

    private int goldSeatsCount;
}
