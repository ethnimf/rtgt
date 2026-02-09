package com.library.libra.controller.api;

import com.library.libra.entity.BorrowRecord;
import com.library.libra.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
public class BorrowApiController {

    private final BorrowService service;

    public BorrowApiController(BorrowService service) {
        this.service = service;
    }

    @GetMapping
    public List<BorrowRecord> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public BorrowRecord one(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BorrowRecord create(@Valid @RequestBody BorrowRecord body) { return service.create(body); }

    @PutMapping("/{id}")
    public BorrowRecord update(@PathVariable Long id, @Valid @RequestBody BorrowRecord body) {
        return service.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
