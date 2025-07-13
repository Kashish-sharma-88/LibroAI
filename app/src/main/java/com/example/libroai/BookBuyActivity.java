package com.example.libroai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.*;

public class BookBuyActivity extends AppCompatActivity {
    private TextView bookTitle, bookAuthor, bookPrice, bookDesc;
    private RadioGroup deliveryOptions;
    private EditText addressInput;
    private ImageView book_image;
    private Button confirmBuyBtn;
    private String selectedDeliveryOption = "Pickup";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_buy);

        bookTitle = findViewById(R.id.book_title);
        bookAuthor = findViewById(R.id.book_author);
        bookPrice = findViewById(R.id.book_price);
        deliveryOptions = findViewById(R.id.delivery_options_buy);
        addressInput = findViewById(R.id.address_input_buy);
        confirmBuyBtn = findViewById(R.id.btn_confirm_buy);
        book_image = findViewById(R.id.book_image);
        bookDesc = findViewById(R.id.book_desc);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String description = intent.getStringExtra("description");
        String imageUrl = intent.getStringExtra("imageUrl");

        bookTitle.setText(title);
        bookAuthor.setText(author);
        bookDesc.setText(description);
        Glide.with(this).load(imageUrl).placeholder(R.drawable.img_2).into(book_image);

        deliveryOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_delivery_buy) {
                selectedDeliveryOption = "Delivery";
                addressInput.setVisibility(View.VISIBLE);
            } else {
                selectedDeliveryOption = "Pickup";
                addressInput.setVisibility(View.GONE);
            }
        });

        // ✅ FINAL CONFIRMED BOOK BUY - FULL FIX
        confirmBuyBtn.setOnClickListener(v -> {
            String address = addressInput.getText().toString().trim();
            if (selectedDeliveryOption.equals("Delivery") && address.length() < 10) {
                Toast.makeText(this, "Please enter valid address", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // ✅ SAME docId for both student and librarian
            String docId = FirebaseFirestore.getInstance().collection("orders").document().getId();

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 7);
            String deliveryDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(cal.getTime());

            Map<String, Object> order = new HashMap<>();
            order.put("title", bookTitle.getText().toString());
            order.put("author", bookAuthor.getText().toString().replace("by ", ""));
            order.put("price", bookPrice.getText().toString());
            order.put("deliveryType", selectedDeliveryOption);
            order.put("address", selectedDeliveryOption.equals("Delivery") ? address : "Pickup from library");
            order.put("status", "Pending");
            order.put("description", bookDesc.getText().toString());
            order.put("studentId", userId);
            order.put("docId", docId); // ✅ SAME docId
            order.put("orderType", "buy"); // ✅ IMPORTANT: Needed in adapter

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("orders")
                    .document(userId)
                    .collection("buy")
                    .document(docId)
                    .set(order)
                    .addOnSuccessListener(aVoid -> {
                        db.collection("ordersForLibrarian")
                                .document("buy")
                                .collection("requests")
                                .document(docId)
                                .set(order)
                                .addOnSuccessListener(unused ->
                                        Toast.makeText(this, "✅ Book Buy Confirmed!", Toast.LENGTH_SHORT).show()
                                )
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "⚠️ Librarian Save Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "❌ Student Save Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });

    }
}