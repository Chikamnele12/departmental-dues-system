package com.demo.departmental_dues_system.controller;

import com.demo.departmental_dues_system.model.Due;
import com.demo.departmental_dues_system.model.Payment;
import com.demo.departmental_dues_system.model.User;
import com.demo.departmental_dues_system.repository.DueRepository;
import com.demo.departmental_dues_system.repository.PaymentRepository;
import com.demo.departmental_dues_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentsController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DueRepository dueRepository;

    @PostMapping("/pay")
    public ResponseEntity<?> makePayment(@RequestParam Long userId,
                                         @RequestParam Long dueId,
                                         @RequestParam Double amount,
                                         @RequestParam(required = false) String reference,
                                         Authentication auth) {

        // Only STUDENT role can make payments
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("STUDENT"))) {
            return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
        }

        User user = userRepository.findById(userId).orElse(null);
        Due due = dueRepository.findById(dueId).orElse(null);

        if (user == null || due == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid user or due ID"));
        }

        // Generate readable reference if not provided
        if (reference == null || reference.isEmpty()) {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String randomSuffix = String.format("%03d", new Random().nextInt(1000)); // 3-digit random
            reference = "PAY-" + user.getLevel() + "-" + user.getMatricNumber().replace("/", "") + "-" + date + "-" + randomSuffix;
        }

        Payment payment = new Payment();
        payment.setStudent(user);
        payment.setDue(due);
        payment.setAmount(amount);
        payment.setStatus(Payment.Status.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setReference(reference);
        paymentRepository.save(payment);

        return ResponseEntity.ok(Map.of("message", "Payment successful", "payment", payment));
    }

    // ADMIN: View all payments
    @GetMapping("/all")
    public ResponseEntity<?> getAllPayments(Authentication auth) {
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
        }

        List<Payment> payments = paymentRepository.findAll();
        return ResponseEntity.ok(payments);
    }

    // ADMIN: View payments by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPaymentsByUser(@PathVariable Long userId, Authentication auth) {
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }

        List<Payment> payments = paymentRepository.findByStudent(user); // Use User entity
        return ResponseEntity.ok(payments);
    }
}
