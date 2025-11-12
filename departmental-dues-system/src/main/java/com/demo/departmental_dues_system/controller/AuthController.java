package com.demo.departmental_dues_system.controller;

import com.demo.departmental_dues_system.dto.LoginRequest;
import com.demo.departmental_dues_system.dto.RegisterRequest;
import com.demo.departmental_dues_system.model.User;
import com.demo.departmental_dues_system.repository.UserRepository;
import com.demo.departmental_dues_system.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists!"));

        if (userRepository.existsByMatricNumber(request.getMatricNumber()))
            return ResponseEntity.badRequest().body(Map.of("error", "Matric number already exists!"));

        User user = new User(
                request.getMatricNumber(),
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getDepartment(),
                request.getFaculty(),
                request.getLevel(),
                request.getRole()
        );

        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Registration successful!");
        response.put("user", Map.of(
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole().toString()
        ));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmailOrMatric())
                .or(() -> userRepository.findByMatricNumber(request.getEmailOrMatric()))
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword()))
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials!"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful!");
        response.put("token", token);
        response.put("user", Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole().toString(),
                "level", user.getLevel()
        ));

        return ResponseEntity.ok(response);
    }
}
