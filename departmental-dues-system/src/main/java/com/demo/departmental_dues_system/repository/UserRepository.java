package com.demo.departmental_dues_system.repository;

import com.demo.departmental_dues_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByMatricNumber(String matricNumber);
    boolean existsByEmail(String email);
    boolean existsByMatricNumber(String matricNumber);
}
