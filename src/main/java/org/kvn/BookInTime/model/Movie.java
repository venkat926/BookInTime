package org.kvn.BookInTime.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.kvn.BookInTime.enums.Genre;

import java.io.Serializable;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private Double rating;

    private Integer totalReviews;

    @OneToMany
    @JsonIgnoreProperties(value = {"movie", "user"})
    private List<Review> reviews;

    @OneToMany
    @JsonIgnoreProperties(value = {"movie", "theater", "bookedTickets", "Seats"})
    private List<Show> shows;


}
