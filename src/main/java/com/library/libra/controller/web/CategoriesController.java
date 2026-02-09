package com.library.libra.controller.web;

import com.library.libra.entity.Category;
import com.library.libra.service.ApiClientFactory;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/librarian/categories")
@PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
public class CategoriesController {

    private final ApiClientFactory api;

    public CategoriesController(ApiClientFactory api) {
        this.api = api;
    }

    @GetMapping
    public String list(Model model) {
        Category[] arr = api.client().get().uri("/api/categories").retrieve().body(Category[].class);
        model.addAttribute("items", arr);
        return "categories/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("item", new Category());
        return "categories/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("item") Category item, BindingResult br) {
        if (br.hasErrors()) return "categories/form";
        api.client().post().uri("/api/categories").body(item).retrieve().toBodilessEntity();
        return "redirect:/librarian/categories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Category item = api.client().get().uri("/api/categories/{id}", id).retrieve().body(Category.class);
        model.addAttribute("item", item);
        return "categories/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("item") Category item, BindingResult br) {
        if (br.hasErrors()) return "categories/form";
        api.client().put().uri("/api/categories/{id}", id).body(item).retrieve().toBodilessEntity();
        return "redirect:/librarian/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        api.client().delete().uri("/api/categories/{id}", id).retrieve().toBodilessEntity();
        return "redirect:/librarian/categories";
    }
}
