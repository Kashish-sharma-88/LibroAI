package com.example.libroai;

public class BookModel {
    String bookId, title, author, description, imageUrl;

    // 🔸 Required no-arg constructor for Firebase
    public BookModel() {
    }

    // 🔸 Full constructor
    public BookModel(String bookId, String title, String author, String description, String imageUrl) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // 🔸 Getters & Setters (Important for Firebase to work)
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
