package org.kvn.BookInTime.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.kvn.BookInTime.enums.ShowTiming;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "shows")
public class Show implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Date showDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShowTiming showTiming;

    @ManyToOne
    @JsonIgnoreProperties(value = {"reviews", "shows"})
    private Movie movie;

    @ManyToOne
    @JsonIgnoreProperties(value = {"shows"})
    private Theater theater;

    @OneToMany
    @JsonIgnoreProperties(value = {"show", "user"})
    private List<Ticket> bookedTickets;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"show", "ticket"})
    private List<Seat> Seats;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

}
