package com.library.libra.controller.api;

import com.library.libra.entity.Author;
import com.library.libra.repository.AuthorRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorApiController {

    private final AuthorRepository repo;

    public AuthorApiController(AuthorRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Author> all() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Author one(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Author create(@Valid @RequestBody Author body) { return repo.save(body); }

    @PutMapping("/{id}")
    public Author update(@PathVariable Long id, @Valid @RequestBody Author body) {
        Author e = repo.findById(id).orElseThrow();
        e.setFullName(body.getFullName());
        return repo.save(e);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
