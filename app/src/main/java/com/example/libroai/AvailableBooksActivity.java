package com.example.libroai;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AvailableBooksActivity extends BaseDrawerActivity {

    private RecyclerView recyclerView;
    private com.example.libroai.bookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout, title, and drawer menu item
        setContent(R.layout.activity_available_books, "Available Books", R.id.available_books);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Book> books = getAvailableBooks();
        bookAdapter = new bookAdapter(books);
        recyclerView.setAdapter(bookAdapter);
    }

    private List<Book> getAvailableBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", "Available"));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "Fiction", "Available"));
        books.add(new Book("1984", "George Orwell", "Fiction", "Available"));
        books.add(new Book("Pride and Prejudice", "Jane Austen", "Romance", "Available"));
        books.add(new Book("The Hobbit", "J.R.R. Tolkien", "Fantasy", "Available"));
        books.add(new Book("Harry Potter", "J.K. Rowling", "Fantasy", "Available"));
        books.add(new Book("The Alchemist", "Paulo Coelho", "Fiction", "Available"));
        books.add(new Book("The Alchemist", "Paulo Coelho", "Fiction", "Available"));
        return books;
    }
}
