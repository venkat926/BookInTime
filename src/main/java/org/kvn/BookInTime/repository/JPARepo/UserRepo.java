package org.kvn.BookInTime.repository.JPARepo;

import org.kvn.BookInTime.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepo extends JpaRepository<Users, Integer> {
    UserDetails findByEmail(String username);
}
