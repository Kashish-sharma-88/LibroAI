package com.example.libroai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import androidx.annotation.Nullable;

public class StdBookDetailActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private String studentId = "student_123"; // TODO: Replace with real student id/email
    private String requestId = null;
    private ListenerRegistration statusListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_book_detail);

        ImageView bookImage = findViewById(R.id.book_image);
        TextView bookTitle = findViewById(R.id.book_title);
        TextView bookAuthor = findViewById(R.id.book_author);
        TextView bookDesc = findViewById(R.id.book_desc);
        Button btnBuy = findViewById(R.id.btn_buy);
        Button btnRent = findViewById(R.id.btn_rent);
        Button btnIssue = findViewById(R.id.btn_issue);
        // Remove status TextView logic

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String description = intent.getStringExtra("description");
        String imageUrl = intent.getStringExtra("imageUrl");

        bookTitle.setText(title);
        bookAuthor.setText(author);
        bookDesc.setText(description);
        Glide.with(this).load(imageUrl).placeholder(R.drawable.img_2).into(bookImage);

        db = FirebaseFirestore.getInstance();

        // Check if request already exists for this student and book
        Query query = db.collection("Issued Books")
            .whereEqualTo("bookTitle", title)
            .whereEqualTo("studentId", studentId);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                // Request exists, get status and listen for updates
                DocumentReference docRef = queryDocumentSnapshots.getDocuments().get(0).getReference();
                requestId = docRef.getId();
                listenToStatus(docRef, btnIssue);
                btnIssue.setEnabled(false);
            } else {
                btnIssue.setEnabled(true);
                btnIssue.setText("Issue");
                btnIssue.setBackgroundResource(R.drawable.rounded_button_brown);
                btnIssue.setTextColor(getResources().getColor(R.color.text_button));
            }
        });

        btnIssue.setOnClickListener(v -> {
            btnIssue.setEnabled(false);
            btnIssue.setText("Requesting...");
            btnIssue.setBackgroundResource(R.drawable.rounded_button_brown);
            btnIssue.setTextColor(getResources().getColor(R.color.text_button));
            // Add request to Firestore
            IssuedBookRequestModel request = new IssuedBookRequestModel(
                null, title, author, imageUrl, studentId, "pending", System.currentTimeMillis()
            );
            db.collection("Issued Books").add(request)
                .addOnSuccessListener(documentReference -> {
                    requestId = documentReference.getId();
                    listenToStatus(documentReference, btnIssue);
                })
                .addOnFailureListener(e -> {
                    btnIssue.setText("Failed. Try Again");
                    btnIssue.setEnabled(true);
                });
        });
    }

    private void listenToStatus(DocumentReference docRef, Button btnIssue) {
        if (statusListener != null) statusListener.remove();
        statusListener = docRef.addSnapshotListener((snapshot, error) -> {
            if (error != null || snapshot == null || !snapshot.exists()) {
                btnIssue.setText("Issue");
                btnIssue.setEnabled(true);
                btnIssue.setBackgroundResource(R.drawable.rounded_button_brown);
                btnIssue.setTextColor(getResources().getColor(R.color.text_button));
                return;
            }
            String status = snapshot.getString("status");
            if ("pending".equals(status)) {
                btnIssue.setText("Pending");
                btnIssue.setEnabled(false);
                btnIssue.setBackgroundResource(R.drawable.rounded_button_grey);
                btnIssue.setTextColor(getResources().getColor(R.color.white));
            } else if ("approved".equals(status)) {
                btnIssue.setText("Issued");
                btnIssue.setEnabled(false);
                btnIssue.setBackgroundResource(R.drawable.rounded_button_green);
                btnIssue.setTextColor(getResources().getColor(R.color.white));
            } else if ("rejected".equals(status)) {
                btnIssue.setText("Not Available");
                btnIssue.setEnabled(false);
                btnIssue.setBackgroundResource(R.drawable.rounded_button_red);
                btnIssue.setTextColor(getResources().getColor(R.color.white));
            } else {
                btnIssue.setText("Issue");
                btnIssue.setEnabled(true);
                btnIssue.setBackgroundResource(R.drawable.rounded_button_brown);
                btnIssue.setTextColor(getResources().getColor(R.color.text_button));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (statusListener != null) statusListener.remove();
    }
} 