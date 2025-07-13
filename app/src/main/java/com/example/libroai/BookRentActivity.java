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

public class BookRentActivity extends AppCompatActivity {
    private TextView bookTitle, bookAuthor, bookPrice, returnDateView, bookDesc;
    private RadioGroup deliveryOptions;
    private EditText addressInput;
    private ImageView book_image;
    private Button confirmRentBtn;
    private String selectedDeliveryOption = "Pickup";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_rent);

        bookTitle = findViewById(R.id.book_title);
        bookAuthor = findViewById(R.id.book_author);
        bookPrice = findViewById(R.id.book_rent_price);
        returnDateView = findViewById(R.id.return_date);
        deliveryOptions = findViewById(R.id.delivery_options_rent);
        addressInput = findViewById(R.id.address_input_rent);
        confirmRentBtn = findViewById(R.id.btn_confirm_rent);
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

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        String returnDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(calendar.getTime());
        returnDateView.setText("Return by: " + returnDate);

        deliveryOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_delivery_rent) {
                selectedDeliveryOption = "Delivery";
                addressInput.setVisibility(View.VISIBLE);
            } else {
                selectedDeliveryOption = "Pickup";
                addressInput.setVisibility(View.GONE);
            }
        });

        confirmRentBtn.setOnClickListener(v -> {
            String address = addressInput.getText().toString().trim();
            if (selectedDeliveryOption.equals("Delivery") && address.length() < 10) {
                Toast.makeText(this, "Please enter valid address", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String docId = FirebaseFirestore.getInstance().collection("orders").document().getId(); // generate once

            String deliveryDate = null;
            if (selectedDeliveryOption.equals("Delivery")) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, 5);
                deliveryDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(cal.getTime());
            }

            Map<String, Object> order = new HashMap<>();
            order.put("title", title);
            order.put("author", author);
            order.put("price", bookPrice.getText().toString());
            order.put("deliveryType", selectedDeliveryOption);
            order.put("address", selectedDeliveryOption.equals("Delivery") ? address : "Pickup from library");
            order.put("status", "Pending");
            order.put("description", description);
            order.put("returnDate", returnDate);
            order.put("studentId", userId); // must have this

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("orders")
                    .document(userId)
                    .collection("rent")
                    .document(docId)
                    .set(order)
                    .addOnSuccessListener(doc -> Toast.makeText(this, "Book Rent Confirmed!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());

            db.collection("ordersForLibrarian")
                    .document("rent")
                    .collection("requests")
                    .document(docId)
                    .set(order);
        });
    }
}
