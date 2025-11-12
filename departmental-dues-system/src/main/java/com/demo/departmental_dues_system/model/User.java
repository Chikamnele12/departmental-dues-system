package com.demo.departmental_dues_system.model;

import jakarta.persistence.*;

@Entity
@Table(name = " class users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String matricNumber;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String department;
    private String faculty;
    private String level;

    @Enumerated(EnumType.STRING)
    private Role role;  // STUDENT or ADMIN

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public enum Status {
        ACTIVE, INACTIVE
    }

    // Constructors
    public User() {}

    public User(String matricNumber, String name, String email, String password,
                String department, String faculty, String level, Role role) {
        this.matricNumber = matricNumber;
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.faculty = faculty;
        this.level = level;
        this.role = role;
        this.status = Status.ACTIVE;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMatricNumber() { return matricNumber; }
    public void setMatricNumber(String matricNumber) { this.matricNumber = matricNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
