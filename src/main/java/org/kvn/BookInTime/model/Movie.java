package org.kvn.BookInTime.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.kvn.BookInTime.enums.Genre;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private Double rating;

    @OneToMany
    @JsonIgnoreProperties(value = {"movie"})
    private List<Review> reviews;

    @OneToMany
    @JsonIgnoreProperties(value = {"movie", "theater", "bookedTickets", "Seats"})
    private List<Show> shows;


}
