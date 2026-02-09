package com.library.libra.controller.web;

import com.library.libra.dto.CopyForm;
import com.library.libra.entity.Book;
import com.library.libra.entity.BookCopy;
import com.library.libra.service.ApiClientFactory;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/librarian/copies")
@PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
public class CopiesController {

    private final ApiClientFactory api;

    public CopiesController(ApiClientFactory api) {
        this.api = api;
    }

    @GetMapping
    public String list(Model model) {
        BookCopy[] arr = api.client().get().uri("/api/copies").retrieve().body(BookCopy[].class);
        model.addAttribute("items", arr);
        return "copies/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new CopyForm());
        fillRefs(model);
        model.addAttribute("statuses", BookCopy.Status.values());
        return "copies/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") CopyForm form, BindingResult br, Model model) {
        if (br.hasErrors()) {
            fillRefs(model);
            model.addAttribute("statuses", BookCopy.Status.values());
            return "copies/form";
        }
        api.client().post().uri("/api/copies").body(toEntity(form)).retrieve().toBodilessEntity();
        return "redirect:/librarian/copies";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        BookCopy c = api.client().get().uri("/api/copies/{id}", id).retrieve().body(BookCopy.class);
        model.addAttribute("form", fromEntity(c));
        fillRefs(model);
        model.addAttribute("statuses", BookCopy.Status.values());
        return "copies/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") CopyForm form, BindingResult br, Model model) {
        if (br.hasErrors()) {
            fillRefs(model);
            model.addAttribute("statuses", BookCopy.Status.values());
            return "copies/form";
        }
        api.client().put().uri("/api/copies/{id}", id).body(toEntity(form)).retrieve().toBodilessEntity();
        return "redirect:/librarian/copies";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        api.client().delete().uri("/api/copies/{id}", id).retrieve().toBodilessEntity();
        return "redirect:/librarian/copies";
    }

    private void fillRefs(Model model) {
        Book[] books = api.client().get().uri("/api/books").retrieve().body(Book[].class);
        model.addAttribute("books", books);
    }

    private BookCopy toEntity(CopyForm f) {
        Book b = new Book(); b.setId(f.getBookId());
        BookCopy c = new BookCopy();
        c.setId(f.getId());
        c.setBook(b);
        c.setInventoryCode(f.getInventoryCode());
        c.setStatus(f.getStatus());
        return c;
    }

    private CopyForm fromEntity(BookCopy c) {
        CopyForm f = new CopyForm();
        f.setId(c.getId());
        f.setBookId(c.getBook() != null ? c.getBook().getId() : null);
        f.setInventoryCode(c.getInventoryCode());
        f.setStatus(c.getStatus());
        return f;
    }
}
