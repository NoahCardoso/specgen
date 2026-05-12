package com.example.demo.output;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(user));
    }

    @GetMapping
    public List<User> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getOne(@PathVariable long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(
            @PathVariable long id,
            @Valid @RequestBody User updated
    ) {
        return ResponseEntity.ok(service.update(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}