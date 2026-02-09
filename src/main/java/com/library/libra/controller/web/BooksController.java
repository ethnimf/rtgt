package com.library.libra.controller.web;

import com.library.libra.dto.BookForm;
import com.library.libra.entity.Author;
import com.library.libra.entity.Book;
import com.library.libra.entity.Category;
import com.library.libra.entity.Publisher;
import com.library.libra.service.ApiClientFactory;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/librarian/books")
@PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
public class BooksController {

    private final ApiClientFactory api;

    public BooksController(ApiClientFactory api) {
        this.api = api;
    }

    @GetMapping
    public String list(Model model) {
        Book[] arr = api.client().get().uri("/api/books").retrieve().body(Book[].class);
        model.addAttribute("items", arr);
        return "books/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        fillRefs(model);
        return "books/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") BookForm form, BindingResult br, Model model) {
        if (br.hasErrors()) {
            fillRefs(model);
            return "books/form";
        }
        api.client().post().uri("/api/books").body(toEntity(form)).retrieve().toBodilessEntity();
        return "redirect:/librarian/books";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Book b = api.client().get().uri("/api/books/{id}", id).retrieve().body(Book.class);
        model.addAttribute("form", fromEntity(b));
        fillRefs(model);
        return "books/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") BookForm form, BindingResult br, Model model) {
        if (br.hasErrors()) {
            fillRefs(model);
            return "books/form";
        }
        api.client().put().uri("/api/books/{id}", id).body(toEntity(form)).retrieve().toBodilessEntity();
        return "redirect:/librarian/books";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        api.client().delete().uri("/api/books/{id}", id).retrieve().toBodilessEntity();
        return "redirect:/librarian/books";
    }

    private void fillRefs(Model model) {
        Author[] authors = api.client().get().uri("/api/authors").retrieve().body(Author[].class);
        Category[] categories = api.client().get().uri("/api/categories").retrieve().body(Category[].class);
        Publisher[] publishers = api.client().get().uri("/api/publishers").retrieve().body(Publisher[].class);
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categories);
        model.addAttribute("publishers", publishers);
    }

    private Book toEntity(BookForm f) {
        Author a = new Author(); a.setId(f.getAuthorId());
        Category c = new Category(); c.setId(f.getCategoryId());
        Publisher p = new Publisher(); p.setId(f.getPublisherId());

        Book b = new Book();
        b.setId(f.getId());
        b.setTitle(f.getTitle());
        b.setIsbn(f.getIsbn());
        b.setPublicationYear(f.getPublicationYear());
        b.setPrice(f.getPrice());
        b.setAuthor(a);
        b.setCategory(c);
        b.setPublisher(p);
        return b;
    }

    private BookForm fromEntity(Book b) {
        BookForm f = new BookForm();
        f.setId(b.getId());
        f.setTitle(b.getTitle());
        f.setIsbn(b.getIsbn());
        f.setPublicationYear(b.getPublicationYear());
        f.setPrice(b.getPrice());
        f.setAuthorId(b.getAuthor() != null ? b.getAuthor().getId() : null);
        f.setCategoryId(b.getCategory() != null ? b.getCategory().getId() : null);
        f.setPublisherId(b.getPublisher() != null ? b.getPublisher().getId() : null);
        return f;
    }
}
