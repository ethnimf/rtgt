package com.library.libra.controller.api;

import com.library.libra.entity.Publisher;
import com.library.libra.repository.PublisherRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
public class PublisherApiController {

    private final PublisherRepository repo;

    public PublisherApiController(PublisherRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Publisher> all() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Publisher one(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Publisher create(@Valid @RequestBody Publisher body) { return repo.save(body); }

    @PutMapping("/{id}")
    public Publisher update(@PathVariable Long id, @Valid @RequestBody Publisher body) {
        Publisher e = repo.findById(id).orElseThrow();
        e.setName(body.getName());
        return repo.save(e);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
