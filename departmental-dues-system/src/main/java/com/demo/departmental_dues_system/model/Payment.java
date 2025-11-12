package com.demo.departmental_dues_system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "class payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link payment to student
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    // Link payment to a due
    @ManyToOne
    @JoinColumn(name = "due_id", nullable = false)
    private Due due;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false, unique = true)
    private String reference; // Payment reference (Paystack)

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate = LocalDateTime.now();

    public enum Status {
        PENDING,
        SUCCESS,
        FAILED
    }

    // Constructors
    public Payment() {}

    public Payment(User student, Due due, Double amount, String reference, Status status) {
        this.student = student;
        this.due = due;
        this.amount = amount;
        this.reference = reference;
        this.status = status;
        this.paymentDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public Due getDue() { return due; }
    public void setDue(Due due) { this.due = due; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
}
