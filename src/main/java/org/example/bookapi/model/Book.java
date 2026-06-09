package org.example.bookapi.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column(name = "book_id")
    private int bookId;

    @NotBlank(message = "Titel darf nicht leer sein")
    @Size(max = 100, message = "Titel darf maximal 100 Zeichen haben")
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @NotBlank(message = "Autor darf nicht leer sein")
    @Size(max = 100, message = "Autor darf maximal 100 Zeichen haben")
    @Column(name = "author", nullable = false, length = 100)
    private String author;

    @NotNull(message = "Erscheinungsdatum darf nicht leer sein")
    @Column(name = "publish_date", nullable = false)
    private LocalDate publishDate;

    @NotNull(message = "Preis darf nicht leer sein")
    @DecimalMin(value = "0.0", inclusive = true, message = "Preis darf nicht negativ sein")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Min(value = 1, message = "Seitenzahl muss mindestens 1 sein")
    @Column(name = "pages", nullable = false)
    private int pages;

    @Column(name = "available", nullable = false)
    private boolean available;

    @JsonBackReference
    @NotNull(message = "Kategorie darf nicht leer sein")
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Book() {
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}