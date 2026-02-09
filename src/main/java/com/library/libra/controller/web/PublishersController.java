package com.library.libra.controller.web;

import com.library.libra.entity.Publisher;
import com.library.libra.service.ApiClientFactory;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/librarian/publishers")
@PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
public class PublishersController {

    private final ApiClientFactory api;

    public PublishersController(ApiClientFactory api) {
        this.api = api;
    }

    @GetMapping
    public String list(Model model) {
        Publisher[] arr = api.client().get().uri("/api/publishers").retrieve().body(Publisher[].class);
        model.addAttribute("items", arr);
        return "publishers/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("item", new Publisher());
        return "publishers/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("item") Publisher item, BindingResult br) {
        if (br.hasErrors()) return "publishers/form";
        api.client().post().uri("/api/publishers").body(item).retrieve().toBodilessEntity();
        return "redirect:/librarian/publishers";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Publisher item = api.client().get().uri("/api/publishers/{id}", id).retrieve().body(Publisher.class);
        model.addAttribute("item", item);
        return "publishers/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("item") Publisher item, BindingResult br) {
        if (br.hasErrors()) return "publishers/form";
        api.client().put().uri("/api/publishers/{id}", id).body(item).retrieve().toBodilessEntity();
        return "redirect:/librarian/publishers";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        api.client().delete().uri("/api/publishers/{id}", id).retrieve().toBodilessEntity();
        return "redirect:/librarian/publishers";
    }
}
