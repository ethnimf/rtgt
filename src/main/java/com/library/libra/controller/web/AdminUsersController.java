package com.library.libra.controller.web;

import com.library.libra.entity.User;
import com.library.libra.service.ApiClientFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsersController {

    private final ApiClientFactory api;

    public AdminUsersController(ApiClientFactory api) {
        this.api = api;
    }

    @GetMapping
    public String list(Model model) {
        User[] users = api.client().get().uri("/api/admin/users").retrieve().body(User[].class);
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        api.client().delete().uri("/api/admin/users/{id}", id).retrieve().toBodilessEntity();
        return "redirect:/admin/users";
    }
}
