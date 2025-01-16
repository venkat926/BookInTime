package org.kvn.BookInTime.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketBookingRequestDTO {

    @NotBlank(message = "ShowId can not be null")
    private Integer showId;

    @NotEmpty(message = "seatNumbers can not ne empty")
    private Set<String> seatNumbers;

}
