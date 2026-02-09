package com.library.libra.config;

import com.library.libra.entity.Role;
import com.library.libra.entity.User;
import com.library.libra.repository.RoleRepository;
import com.library.libra.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            Role user = roleRepo.findByName("ROLE_USER").orElseGet(() -> roleRepo.save(makeRole("ROLE_USER")));
            Role librarian = roleRepo.findByName("ROLE_LIBRARIAN").orElseGet(() -> roleRepo.save(makeRole("ROLE_LIBRARIAN")));
            Role admin = roleRepo.findByName("ROLE_ADMIN").orElseGet(() -> roleRepo.save(makeRole("ROLE_ADMIN")));

            // admin: admin@local / admin123
            if (!userRepo.existsByEmail("admin@local")) {
                User u = new User();
                u.setEmail("admin@local");
                u.setUsername("admin@local");
                u.setPassword(encoder.encode("admin123"));
                u.setEnabled(true);
                u.setRoles(Set.of(admin, librarian, user));
                userRepo.save(u);
            }
        };
    }

    private Role makeRole(String name) {
        Role r = new Role();
        r.setName(name);
        return r;
    }
}
