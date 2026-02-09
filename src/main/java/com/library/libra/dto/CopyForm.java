package com.library.libra.dto;

import com.library.libra.entity.BookCopy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CopyForm {

    private Long id;

    @NotNull(message = "Книга обязательна")
    private Long bookId;

    @NotBlank(message = "Инвентарный код обязателен")
    private String inventoryCode;

    @NotNull(message = "Статус обязателен")
    private BookCopy.Status status = BookCopy.Status.AVAILABLE;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public String getInventoryCode() { return inventoryCode; }
    public void setInventoryCode(String inventoryCode) { this.inventoryCode = inventoryCode; }

    public BookCopy.Status getStatus() { return status; }
    public void setStatus(BookCopy.Status status) { this.status = status; }
}
