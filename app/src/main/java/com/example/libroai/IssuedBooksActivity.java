package com.example.libroai;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class IssuedBooksActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private com.example.libroai.bookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_books);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Book> books = getIssuedBooks();
        bookAdapter = new bookAdapter(books);
        recyclerView.setAdapter(bookAdapter);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private List<Book> getIssuedBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("The Catcher in the Rye", "J.D. Salinger", "Fiction", "Issued"));
        books.add(new Book("Lord of the Flies", "William Golding", "Fiction", "Issued"));
        books.add(new Book("Animal Farm", "George Orwell", "Fiction", "Issued"));
        return books;
    }
}