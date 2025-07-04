package com.example.libroai;

public class Book {
    private String title;
    private String author;
    private String category;
    private String status;

    public Book(String title, String author, String category, String status) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }
}