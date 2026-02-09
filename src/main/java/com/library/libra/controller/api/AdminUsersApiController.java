package com.library.libra.controller.api;

import com.library.libra.entity.User;
import com.library.libra.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsersApiController {

    private final UserRepository repo;

    public AdminUsersApiController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<User> all() { return repo.findAll(); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
