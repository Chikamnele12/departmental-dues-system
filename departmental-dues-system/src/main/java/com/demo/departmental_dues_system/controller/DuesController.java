package com.demo.departmental_dues_system.controller;

import com.demo.departmental_dues_system.model.Due;
import com.demo.departmental_dues_system.repository.DueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dues")
@CrossOrigin(origins = "*")
public class DuesController {

    @Autowired
    private DueRepository dueRepository;

    // STUDENT & ADMIN: View all dues
    @GetMapping("/all")
    public ResponseEntity<List<Due>> getAllDues() {
        return ResponseEntity.ok(dueRepository.findAll());
    }

    // ADMIN: Create a new due
    @PostMapping("/create")
    public ResponseEntity<?> createDue(@RequestBody Due due, Authentication auth) {
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
        }

        dueRepository.save(due);
        return ResponseEntity.ok(Map.of("message", "Due created successfully", "due", due));
    }

    // ADMIN: Update a due
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDue(@PathVariable Long id, @RequestBody Due dueDetails, Authentication auth) {
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
        }

        Due due = dueRepository.findById(id).orElse(null);
        if (due == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Due not found"));
        }

        due.setTitle(dueDetails.getTitle());
        due.setAmount(dueDetails.getAmount());
        due.setDescription(dueDetails.getDescription());
        dueRepository.save(due);

        return ResponseEntity.ok(Map.of("message", "Due updated successfully", "due", due));
    }

    // ADMIN: Delete a due
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDue(@PathVariable Long id, Authentication auth) {
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
        }

        Due due = dueRepository.findById(id).orElse(null);
        if (due == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Due not found"));
        }

        dueRepository.delete(due);
        return ResponseEntity.ok(Map.of("message", "Due deleted successfully"));
    }
}
