package com.demo.departmental_dues_system.repository;

import com.demo.departmental_dues_system.model.Due;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DueRepository extends JpaRepository<Due, Long> {
    // Additional query methods if needed
}
