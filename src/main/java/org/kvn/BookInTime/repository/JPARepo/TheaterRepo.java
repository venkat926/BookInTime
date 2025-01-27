package org.kvn.BookInTime.repository.JPARepo;

import org.kvn.BookInTime.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepo extends JpaRepository<Theater, Integer> {
}
