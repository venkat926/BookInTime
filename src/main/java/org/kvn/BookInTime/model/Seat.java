package org.kvn.BookInTime.model;

import jakarta.persistence.*;
import lombok.*;
import org.kvn.BookInTime.enums.SeatType;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer seatNo;

    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    private boolean isBooked;

    private int price;

    @ManyToOne
    private Show show;

    @ManyToOne
    private Ticket ticket;

    private Date bookingTime;
}
