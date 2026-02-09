package com.library.libra.controller.web;

import com.library.libra.dto.RegisterRequest;
import com.library.libra.entity.Role;
import com.library.libra.entity.User;
import com.library.libra.repository.RoleRepository;
import com.library.libra.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("req", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("req") RegisterRequest req,
                           BindingResult bindingResult) {

        if (!req.getPassword().equals(req.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Пароли не совпадают");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            bindingResult.rejectValue("email", "email.exists", "Email уже занят");
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        User u = new User();
        u.setEmail(req.getEmail());
        u.setUsername(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setEnabled(true);
        u.setRoles(Set.of(userRole));

        userRepository.save(u);
        return "redirect:/login?registered";
    }
}
