package com.demo.departmental_dues_system.repository;

import com.demo.departmental_dues_system.model.Payment;
import com.demo.departmental_dues_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find all payments by User entity
    List<Payment> findByStudent(User student);

    // Or find by user id (optional)
    List<Payment> findByStudentId(Long studentId);
}
