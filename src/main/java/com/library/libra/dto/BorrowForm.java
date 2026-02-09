package com.library.libra.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class BorrowForm {

    private Long id;

    @NotNull(message = "Читатель обязателен")
    private Long memberId;

    @NotNull(message = "Экземпляр книги обязателен")
    private Long copyId;

    @NotNull(message = "Дата выдачи обязательна")
    private LocalDate borrowedAt;

    @NotNull(message = "Срок возврата обязателен")
    private LocalDate dueAt;

    private LocalDate returnedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }

    public Long getCopyId() { return copyId; }
    public void setCopyId(Long copyId) { this.copyId = copyId; }

    public LocalDate getBorrowedAt() { return borrowedAt; }
    public void setBorrowedAt(LocalDate borrowedAt) { this.borrowedAt = borrowedAt; }

    public LocalDate getDueAt() { return dueAt; }
    public void setDueAt(LocalDate dueAt) { this.dueAt = dueAt; }

    public LocalDate getReturnedAt() { return returnedAt; }
    public void setReturnedAt(LocalDate returnedAt) { this.returnedAt = returnedAt; }
}
