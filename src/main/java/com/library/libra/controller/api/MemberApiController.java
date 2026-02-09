package com.library.libra.controller.api;

import com.library.libra.entity.Member;
import com.library.libra.repository.MemberRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberRepository repo;

    public MemberApiController(MemberRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Member> all() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Member one(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Member create(@Valid @RequestBody Member body) { return repo.save(body); }

    @PutMapping("/{id}")
    public Member update(@PathVariable Long id, @Valid @RequestBody Member body) {
        Member e = repo.findById(id).orElseThrow();
        e.setFullName(body.getFullName());
        e.setEmail(body.getEmail());
        e.setPhone(body.getPhone());
        return repo.save(e);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
