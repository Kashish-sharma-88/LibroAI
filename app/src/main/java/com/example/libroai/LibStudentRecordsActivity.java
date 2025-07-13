package com.example.libroai;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LibStudentRecordsActivity extends AppCompatActivity implements LibStudentRecordsAdapter.OnRecordClickListener {

    private RecyclerView recordsRecyclerView;
    private LibStudentRecordsAdapter adapter;
    private List<LibStudentRecordModel> allRecords;
    private List<LibStudentRecordModel> filteredRecords;
    private EditText searchEditText;
    private LinearLayout emptyStateLayout;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_student_records);

        // Initialize views
        initViews();
        setupRecyclerView();
        loadIssuedBookRecords();
        setupSearchFunctionality();
        Toast.makeText(this, "LibStudentRecordsActivity Opened", Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        recordsRecyclerView = findViewById(R.id.recordsRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        
        // Setup back button
        android.widget.ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        allRecords = new ArrayList<>();
        filteredRecords = new ArrayList<>();
        adapter = new LibStudentRecordsAdapter(this, filteredRecords, this);
        recordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recordsRecyclerView.setAdapter(adapter);
    }

    private void loadIssuedBookRecords() {
        db = FirebaseFirestore.getInstance();
        allRecords.clear();
        filteredRecords.clear();

        db.collection("Issued Books")
            .whereIn("status", java.util.Arrays.asList("approved", "ISSUED"))
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String studentId = doc.getString("studentId");
                    String studentName = doc.getString("studentName");
                    String studentEmail = doc.getString("studentEmail");
                    String bookId = doc.getString("bookId");
                    String bookTitle = doc.getString("bookTitle");
                    String bookAuthor = doc.getString("bookAuthor");
                    String bookCategory = doc.getString("bookCategory");
                    String issueDate = doc.getString("issueDate");
                    String returnDate = doc.getString("returnDate");
                    String status = doc.getString("status");
                    int daysRemaining = doc.contains("daysRemaining") && doc.get("daysRemaining") != null ? doc.getLong("daysRemaining").intValue() : 0;
                    double fineAmount = doc.contains("fineAmount") && doc.get("fineAmount") != null ? doc.getDouble("fineAmount") : 0.0;
                    String studentPhone = doc.getString("studentPhone");

                    LibStudentRecordModel record = new LibStudentRecordModel(
                        studentId, studentName, studentEmail, bookId, bookTitle, bookAuthor, bookCategory,
                        issueDate, returnDate, status, daysRemaining, fineAmount, studentPhone
                    );
                    allRecords.add(record);
                }
                filteredRecords.addAll(allRecords);
                adapter.notifyDataSetChanged();
                updateEmptyState();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to load records: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    private void setupSearchFunctionality() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRecords(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterRecords(String query) {
        filteredRecords.clear();
        
        if (query.isEmpty()) {
            filteredRecords.addAll(allRecords);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (LibStudentRecordModel record : allRecords) {
                if ((record.getStudentName() != null && record.getStudentName().toLowerCase().contains(lowerCaseQuery)) ||
                    (record.getBookTitle() != null && record.getBookTitle().toLowerCase().contains(lowerCaseQuery)) ||
                    (record.getStudentEmail() != null && record.getStudentEmail().toLowerCase().contains(lowerCaseQuery)) ||
                    (record.getBookAuthor() != null && record.getBookAuthor().toLowerCase().contains(lowerCaseQuery))) {
                    filteredRecords.add(record);
                }
            }
        }
        
        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (filteredRecords.isEmpty()) {
            emptyStateLayout.setVisibility(View.VISIBLE);
            recordsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
            recordsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewDetailsClick(LibStudentRecordModel record) {
        // Launch student detail activity
        Intent intent = new Intent(this, LibStudentDetailActivity.class);
        intent.putExtra("studentId", record.getStudentId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
} 