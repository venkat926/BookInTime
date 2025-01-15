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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer seatNo;

    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    private boolean isBooked;

    @Column(nullable = false)
    private int price;

    @ManyToOne
    private Show show;

    @ManyToOne
    private Ticket ticket;

}
