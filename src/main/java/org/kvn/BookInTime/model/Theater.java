package org.kvn.BookInTime.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.kvn.BookInTime.enums.City;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theater implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    private String address;

    @Enumerated(EnumType.STRING)
    private City city;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"movie", "theater", "bookedTickets", "Seats"})
    private List<Show> shows;

    private int regularSeatsCount;

    private int goldSeatsCount;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

}
