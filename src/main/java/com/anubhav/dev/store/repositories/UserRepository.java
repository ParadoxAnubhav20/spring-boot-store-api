package com.anubhav.dev.store.repositories;

import com.anubhav.dev.store.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User getById(Long id);

    boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);
}
