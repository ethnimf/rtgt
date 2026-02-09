package com.library.libra.service;

import com.library.libra.entity.BookCopy;
import com.library.libra.entity.BorrowRecord;
import com.library.libra.repository.BookCopyRepository;
import com.library.libra.repository.BorrowRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowService {

    private final BorrowRecordRepository borrowRepo;
    private final BookCopyRepository copyRepo;

    public BorrowService(BorrowRecordRepository borrowRepo, BookCopyRepository copyRepo) {
        this.borrowRepo = borrowRepo;
        this.copyRepo = copyRepo;
    }

    public List<BorrowRecord> findAll() {
        return borrowRepo.findAll();
    }

    public BorrowRecord findById(Long id) {
        return borrowRepo.findById(id).orElseThrow();
    }

    @Transactional
    public BorrowRecord create(BorrowRecord r) {
        if (r.getBorrowedAt() == null) r.setBorrowedAt(LocalDate.now());
        // mark copy borrowed
        BookCopy copy = copyRepo.findById(r.getBookCopy().getId()).orElseThrow();
        copy.setStatus(BookCopy.Status.BORROWED);
        copyRepo.save(copy);

        return borrowRepo.save(r);
    }

    @Transactional
    public BorrowRecord update(Long id, BorrowRecord form) {
        BorrowRecord r = findById(id);

        r.setMember(form.getMember());
        r.setBookCopy(form.getBookCopy());
        r.setBorrowedAt(form.getBorrowedAt());
        r.setDueAt(form.getDueAt());
        r.setReturnedAt(form.getReturnedAt());

        // if returnedAt set -> make copy available
        if (r.getReturnedAt() != null) {
            BookCopy copy = copyRepo.findById(r.getBookCopy().getId()).orElseThrow();
            copy.setStatus(BookCopy.Status.AVAILABLE);
            copyRepo.save(copy);
        }

        return borrowRepo.save(r);
    }

    @Transactional
    public void delete(Long id) {
        borrowRepo.deleteById(id);
    }
}
