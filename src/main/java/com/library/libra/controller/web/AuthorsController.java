package com.library.libra.controller.web;

import com.library.libra.entity.Author;
import com.library.libra.service.ApiClientFactory;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@Controller
@RequestMapping("/librarian/authors")
@PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
public class AuthorsController {

    private final ApiClientFactory api;

    public AuthorsController(ApiClientFactory api) {
        this.api = api;
    }

    @GetMapping
    public String list(Model model) {
        RestClient c = api.client();
        List<Author> items = c.get().uri("/api/authors").retrieve().body(List.class);
        // RestClient loses generics; fetch as Author[] instead
        Author[] arr = c.get().uri("/api/authors").retrieve().body(Author[].class);
        model.addAttribute("items", arr);
        return "authors/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("item", new Author());
        return "authors/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("item") Author item, BindingResult br) {
        if (br.hasErrors()) return "authors/form";
        api.client().post().uri("/api/authors").body(item).retrieve().toBodilessEntity();
        return "redirect:/librarian/authors";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Author item = api.client().get().uri("/api/authors/{id}", id).retrieve().body(Author.class);
        model.addAttribute("item", item);
        return "authors/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("item") Author item, BindingResult br) {
        if (br.hasErrors()) return "authors/form";
        api.client().put().uri("/api/authors/{id}", id).body(item).retrieve().toBodilessEntity();
        return "redirect:/librarian/authors";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        api.client().delete().uri("/api/authors/{id}", id).retrieve().toBodilessEntity();
        return "redirect:/librarian/authors";
    }
}
