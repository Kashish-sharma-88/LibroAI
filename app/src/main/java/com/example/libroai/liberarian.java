package com.example.libroai;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class liberarian extends AppCompatActivity {

    EditText nameEt, addressEt, areaEt, mobileEt;
    Button submitBtn;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liberarian);

        nameEt = findViewById(R.id.libraryName);
        addressEt = findViewById(R.id.libraryAddress);
        areaEt = findViewById(R.id.libraryArea);
        mobileEt = findViewById(R.id.libraryMobile);
        submitBtn = findViewById(R.id.submitLibraryBtn);

        dbRef = FirebaseDatabase.getInstance().getReference("libraries");

        submitBtn.setOnClickListener(v -> submitLibrary());
    }

    private void submitLibrary() {
        String name = nameEt.getText().toString().trim();
        String address = addressEt.getText().toString().trim();
        String area = areaEt.getText().toString().trim();
        String mobile = mobileEt.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) ||
                TextUtils.isEmpty(area) || TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String key = dbRef.push().getKey();
        LiberaryModel library = new LiberaryModel(name, address, area, mobile);

        dbRef.child(key).setValue(library)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Library Added Successfully", Toast.LENGTH_SHORT).show();
                    // ✅ Redirect to list
                    Intent intent = new Intent(liberarian.this, liberary_list.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
