package com.example.libroai;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AvailableBooksActivity extends BaseDrawerActivity {

    private RecyclerView recyclerView;
    private com.example.libroai.bookAdapter bookAdapter;
    private List<Book> books;
    private LinearLayout emptyState;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout, title, and drawer menu item
        setContent(R.layout.activity_available_books, "Available Books", R.id.available_books);

        db = FirebaseFirestore.getInstance();
        books = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        emptyState = findViewById(R.id.empty_state);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookAdapter = new bookAdapter(books);
        recyclerView.setAdapter(bookAdapter);

        // Fetch books from Firestore
        fetchAvailableBooks();
    }

    private void fetchAvailableBooks() {
        db.collection("Available_Book")
                .whereEqualTo("status", "Available")
                .get()
                .addOnSuccessListener(this::onBooksFetched)
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading books: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void onBooksFetched(QuerySnapshot queryDocumentSnapshots) {
        books.clear();

        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
            String title = document.getString("title");
            String author = document.getString("author");
            String description = document.getString("description");
            String status = document.getString("status");
            String category = document.getString("category");

            if (title != null && author != null) {
                Book book = new Book(title, author, category != null ? category : "General", status != null ? status : "Available");
                books.add(book);
            }
        }

        // Update UI based on data
        if (books.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
            bookAdapter.notifyDataSetChanged();
        }
    }
}
