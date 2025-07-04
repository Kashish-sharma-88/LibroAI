package com.example.libroai;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class LibraryListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private libraryAdapter adapter;
    private List<library> libraryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_list);

        recyclerView = findViewById(R.id.library_recycler_view);
        ImageView backButton = findViewById(R.id.back_button);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initializeLibraryData();

        adapter = new libraryAdapter(libraryList);
        recyclerView.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initializeLibraryData() {
        libraryList = new ArrayList<>();

        // Adding 10 libraries from the provided list with dummy phone numbers
        libraryList.add(new library("Aarvanya Library", "189, Near Pratap Hotel, Shankaracharya Nagar, Deoki Nagar, Yashoda Nagar, Kanpur - 208011 (Uttar Pradesh) India", "+91 98765 43210"));
        libraryList.add(new library("ALL TOPPERS LIBRARY", "250A/2 Rajnagar, Chakeri, Kanpur - 208008 (Uttar Pradesh) India", "+91 98765 43211"));
        libraryList.add(new library("Alpha Library", "299 E 1, Near Sengar Chauraha, Jawahar Puram, Shyam Nagar, Kanpur - 208015 (Uttar Pradesh) India", "+91 98765 43212"));
        libraryList.add(new library("ALPHA LIBRARY", "117, N 303, Rani Ganj, Ambedkar Nagar, Navin Nagar, Kakadeo, Kanpur - 208025 (Uttar Pradesh) India", "+91 98765 43213"));
        libraryList.add(new library("Anupama Gyan Library", "129, O Block, Deoki Nagar, Yashoda Nagar, Kanpur - 208011 (Uttar Pradesh) India", "+91 98765 43214"));
        libraryList.add(new library("Aryam Digital Library", "117/N/115 Kanchan Bhawan, Kakadeo, Kanpur - 208025 (Uttar Pradesh) India", "+91 98765 43215"));
        libraryList.add(new library("Aspirants Library", "2A/208 AWAS VIKAS, Hanspuram, Naubasta, Kanpur - 208021 (Uttar Pradesh) India", "+91 98765 43216"));
        libraryList.add(new library("Aspirants Library 2.0", "117/Q, 702 A-1, Sharda Nagar Road, Near Chhapera Pulia, Sharda Nagar, Kanpur - 208025 (Uttar Pradesh) India", "+91 98765 43217"));
        libraryList.add(new library("Aspire Library", "15, Barra Bypass Road, Barra World Bank, Barra, Kanpur - 208027 (Uttar Pradesh) India", "+91 98765 43218"));
        libraryList.add(new library("Babu Ji Library", "681/5, Taudhakpur Road, Naubasta, Kanpur - 208021 (Uttar Pradesh) India", "+91 98765 43219"));
    }
}