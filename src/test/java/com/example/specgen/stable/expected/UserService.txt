package com.example.demo.output;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User create(User user) {
        return repo.save(user);
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    public User findById(long id) {
        return repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public User update(long id, User updated) {
        User existing = findById(id);

        existing.setEmail(updated.getEmail());
        existing.setPasswordHash(updated.getPasswordHash());
        existing.setAge(updated.getAge());

        return repo.save(existing);
    }

    public void delete(long id) {
        findById(id);
        repo.deleteById(id);
    }
}