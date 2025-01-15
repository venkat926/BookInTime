package org.kvn.BookInTime.repository;

import org.kvn.BookInTime.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepo extends JpaRepository<Seat, Integer> {
}
