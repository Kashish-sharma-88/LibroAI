package com.example.libroai;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class liberay_book extends AppCompatActivity {

    EditText bookTitle, bookAuthor, bookDesc, bookImage;
    AppCompatButton addBookBtn;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_liberay_book);

        bookTitle = findViewById(R.id.bookTitle);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookDesc = findViewById(R.id.bookDesc);
        bookImage = findViewById(R.id.bookImage);
        addBookBtn = findViewById(R.id.loginBtn);

        db = FirebaseFirestore.getInstance();

        // ➕ Add Book
        addBookBtn.setOnClickListener(v -> {
            String title = bookTitle.getText().toString().trim();
            String author = bookAuthor.getText().toString().trim();
            String desc = bookDesc.getText().toString().trim();
            String image = bookImage.getText().toString().trim();

            if (title.isEmpty() || author.isEmpty()) {
                Toast.makeText(this, "📕 Please enter Title and Author", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create book data for Firestore
            Map<String, Object> book = new HashMap<>();
            book.put("title", title);
            book.put("author", author);
            book.put("description", desc.isEmpty() ? "No description available" : desc);
            book.put("imageUrl", image.isEmpty() ? "" : image);
            book.put("status", "Available");
            book.put("addedDate", Timestamp.now());
            book.put("category", "General"); // Default category

            // Save to Firestore
            db.collection("Available_Book")
                    .add(book)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "📚 Book Added Successfully!", Toast.LENGTH_SHORT).show();
                        clearFields();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "❌ Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void clearFields() {
        bookTitle.setText("");
        bookAuthor.setText("");
        bookDesc.setText("");
        bookImage.setText("");
    }
}
