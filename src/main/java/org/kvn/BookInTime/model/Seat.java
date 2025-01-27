package org.kvn.BookInTime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.kvn.BookInTime.enums.SeatType;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat implements Serializable {

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
    @JsonIgnore
    private Show show;

    @ManyToOne
    @JsonIgnore
    private Ticket ticket;

}
