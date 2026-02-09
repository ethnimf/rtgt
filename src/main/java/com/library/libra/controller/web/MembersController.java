package com.library.libra.controller.web;

import com.library.libra.entity.Member;
import com.library.libra.service.ApiClientFactory;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/librarian/members")
@PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
public class MembersController {

    private final ApiClientFactory api;

    public MembersController(ApiClientFactory api) {
        this.api = api;
    }

    @GetMapping
    public String list(Model model) {
        Member[] arr = api.client().get().uri("/api/members").retrieve().body(Member[].class);
        model.addAttribute("items", arr);
        return "members/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("item", new Member());
        return "members/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("item") Member item, BindingResult br) {
        if (br.hasErrors()) return "members/form";
        api.client().post().uri("/api/members").body(item).retrieve().toBodilessEntity();
        return "redirect:/librarian/members";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Member item = api.client().get().uri("/api/members/{id}", id).retrieve().body(Member.class);
        model.addAttribute("item", item);
        return "members/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("item") Member item, BindingResult br) {
        if (br.hasErrors()) return "members/form";
        api.client().put().uri("/api/members/{id}", id).body(item).retrieve().toBodilessEntity();
        return "redirect:/librarian/members";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        api.client().delete().uri("/api/members/{id}", id).retrieve().toBodilessEntity();
        return "redirect:/librarian/members";
    }
}
