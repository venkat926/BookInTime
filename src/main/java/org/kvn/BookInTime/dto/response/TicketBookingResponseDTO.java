package org.kvn.BookInTime.dto.response;

import lombok.*;
import org.kvn.BookInTime.enums.ShowTiming;
import org.kvn.BookInTime.enums.TicketStatus;
import org.kvn.BookInTime.model.Seat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketBookingResponseDTO {

    private TicketStatus ticketStatus;

    private Double ticketPrice;

    private Date showdate;

    private ShowTiming showTiming;

    private String theaterName;

    private String userName;

    private List<Seat> bookedSeats;

    private Date bookedAt;

}
