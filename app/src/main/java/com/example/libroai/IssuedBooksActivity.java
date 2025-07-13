package com.example.libroai;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class IssuedBooksActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private com.example.libroai.bookAdapter bookAdapter;
    private FirebaseFirestore db;
    ImageView backbtn;
    private String currentStudentEmail = "student_123"; // Firestore ke document ke hisaab se

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_books);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backbtn = findViewById(R.id.issueback);
        db = FirebaseFirestore.getInstance();

        fetchIssuedBooks();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void fetchIssuedBooks() {
        db.collection("Issued Books")
            .whereEqualTo("studentId", currentStudentEmail)
            .whereEqualTo("status", "approved")
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<Book> books = new ArrayList<>();
                Log.d("IssuedBooksActivity", "DEBUG: Query size = " + queryDocumentSnapshots.size());
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String title = doc.getString("bookTitle");
                    String author = doc.getString("bookAuthor");
                    String description = doc.getString("bookDescription");
                    String category = "Issued"; // Or fetch from doc if available
                    String status = "Issued";
                    books.add(new Book(title, author, category, status , description));
                }
                bookAdapter = new bookAdapter(books);
                recyclerView.setAdapter(bookAdapter);
            })
            .addOnFailureListener(e -> {
                Log.e("IssuedBooksActivity", "Firestore query failed: ", e);
            });
    }
}