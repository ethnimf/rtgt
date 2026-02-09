package com.library.libra.controller.api;

import com.library.libra.entity.Book;
import com.library.libra.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookApiController {

    private final BookRepository repo;

    public BookApiController(BookRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Book> all() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Book one(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@Valid @RequestBody Book body) { return repo.save(body); }

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @Valid @RequestBody Book body) {
        Book e = repo.findById(id).orElseThrow();
        e.setTitle(body.getTitle());
        e.setIsbn(body.getIsbn());
        e.setPublicationYear(body.getPublicationYear());
        e.setPrice(body.getPrice());
        e.setAuthor(body.getAuthor());
        e.setCategory(body.getCategory());
        e.setPublisher(body.getPublisher());
        return repo.save(e);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
