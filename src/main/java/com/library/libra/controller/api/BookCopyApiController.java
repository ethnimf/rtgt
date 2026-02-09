package com.library.libra.controller.api;

import com.library.libra.entity.BookCopy;
import com.library.libra.repository.BookCopyRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/copies")
public class BookCopyApiController {

    private final BookCopyRepository repo;

    public BookCopyApiController(BookCopyRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<BookCopy> all() { return repo.findAll(); }

    @GetMapping("/{id}")
    public BookCopy one(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookCopy create(@Valid @RequestBody BookCopy body) { return repo.save(body); }

    @PutMapping("/{id}")
    public BookCopy update(@PathVariable Long id, @Valid @RequestBody BookCopy body) {
        BookCopy e = repo.findById(id).orElseThrow();
        e.setBook(body.getBook());
        e.setInventoryCode(body.getInventoryCode());
        e.setStatus(body.getStatus());
        return repo.save(e);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
