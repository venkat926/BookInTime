package org.kvn.BookInTime.repository;

import org.kvn.BookInTime.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepo extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByUserId(Integer id);
}
