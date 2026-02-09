package com.library.libra.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class BookForm {

    private Long id;

    @NotBlank(message = "Название обязательно")
    private String title;

    @NotBlank(message = "ISBN обязателен")
    private String isbn;

    @Min(value = 1500, message = "Год некорректен")
    @Max(value = 2100, message = "Год некорректен")
    private Integer publicationYear;

    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть > 0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @NotNull(message = "Автор обязателен")
    private Long authorId;

    @NotNull(message = "Категория обязательна")
    private Long categoryId;

    @NotNull(message = "Издательство обязательно")
    private Long publisherId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }
}
