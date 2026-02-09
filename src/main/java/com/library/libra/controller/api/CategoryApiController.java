package com.library.libra.controller.api;

import com.library.libra.entity.Category;
import com.library.libra.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryApiController {

    private final CategoryRepository repo;

    public CategoryApiController(CategoryRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Category> all() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Category one(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@Valid @RequestBody Category body) { return repo.save(body); }

    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @Valid @RequestBody Category body) {
        Category e = repo.findById(id).orElseThrow();
        e.setName(body.getName());
        return repo.save(e);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
