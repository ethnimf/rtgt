package com.library.libra.controller.web;

import com.library.libra.dto.BorrowForm;
import com.library.libra.entity.BookCopy;
import com.library.libra.entity.BorrowRecord;
import com.library.libra.entity.Member;
import com.library.libra.service.ApiClientFactory;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/librarian/borrows")
@PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
public class BorrowsController {

    private final ApiClientFactory api;

    public BorrowsController(ApiClientFactory api) {
        this.api = api;
    }

    @GetMapping
    public String list(Model model) {
        BorrowRecord[] arr = api.client().get().uri("/api/borrows").retrieve().body(BorrowRecord[].class);
        model.addAttribute("items", arr);
        return "borrows/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        BorrowForm f = new BorrowForm();
        f.setBorrowedAt(LocalDate.now());
        f.setDueAt(LocalDate.now().plusDays(14));
        model.addAttribute("form", f);
        fillRefs(model);
        return "borrows/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") BorrowForm form, BindingResult br, Model model) {
        if (br.hasErrors()) {
            fillRefs(model);
            return "borrows/form";
        }
        api.client().post().uri("/api/borrows").body(toEntity(form)).retrieve().toBodilessEntity();
        return "redirect:/librarian/borrows";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        BorrowRecord r = api.client().get().uri("/api/borrows/{id}", id).retrieve().body(BorrowRecord.class);
        model.addAttribute("form", fromEntity(r));
        fillRefs(model);
        return "borrows/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") BorrowForm form, BindingResult br, Model model) {
        if (br.hasErrors()) {
            fillRefs(model);
            return "borrows/form";
        }
        api.client().put().uri("/api/borrows/{id}", id).body(toEntity(form)).retrieve().toBodilessEntity();
        return "redirect:/librarian/borrows";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        api.client().delete().uri("/api/borrows/{id}", id).retrieve().toBodilessEntity();
        return "redirect:/librarian/borrows";
    }

    private void fillRefs(Model model) {
        Member[] members = api.client().get().uri("/api/members").retrieve().body(Member[].class);
        BookCopy[] copies = api.client().get().uri("/api/copies").retrieve().body(BookCopy[].class);
        model.addAttribute("members", members);
        model.addAttribute("copies", copies);
    }

    private BorrowRecord toEntity(BorrowForm f) {
        Member m = new Member(); m.setId(f.getMemberId());
        BookCopy c = new BookCopy(); c.setId(f.getCopyId());

        BorrowRecord r = new BorrowRecord();
        r.setId(f.getId());
        r.setMember(m);
        r.setBookCopy(c);
        r.setBorrowedAt(f.getBorrowedAt());
        r.setDueAt(f.getDueAt());
        r.setReturnedAt(f.getReturnedAt());
        return r;
    }

    private BorrowForm fromEntity(BorrowRecord r) {
        BorrowForm f = new BorrowForm();
        f.setId(r.getId());
        f.setMemberId(r.getMember() != null ? r.getMember().getId() : null);
        f.setCopyId(r.getBookCopy() != null ? r.getBookCopy().getId() : null);
        f.setBorrowedAt(r.getBorrowedAt());
        f.setDueAt(r.getDueAt());
        f.setReturnedAt(r.getReturnedAt());
        return f;
    }
}
