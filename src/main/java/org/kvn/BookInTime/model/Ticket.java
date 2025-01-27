package org.kvn.BookInTime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.kvn.BookInTime.enums.TicketStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    private Double amount;

    @ManyToOne
    @JsonIgnoreProperties(value = {"bookedTickets", "seats"})
    private Show show;

    @ManyToOne
    @JsonIgnore
    private Users user;

    @OneToMany
    @JsonIgnoreProperties(value = {"movie", "theater", "bookedTickets", "Seats"})
    private List<Seat> bookedSeats;

    @CreationTimestamp
    private Date bookingTime;

    @UpdateTimestamp
    private Date updateTime;

}
