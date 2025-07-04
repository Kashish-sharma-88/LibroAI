package com.example.libroai;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class liberary_list extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LiberaryAdapter adapter;
    private List<LiberaryModel> fullList;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liberary_list2);

        recyclerView = findViewById(R.id.libraryRecyclerView);
        searchBar = findViewById(R.id.searchBar);
        fullList = new ArrayList<>();

        adapter = new LiberaryAdapter(fullList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchLibraries();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }
        });
    }

    private void fetchLibraries() {
        FirebaseDatabase.getInstance().getReference("libraries")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        fullList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            LiberaryModel lib = snap.getValue(LiberaryModel.class);
                            if (lib != null) {
                                fullList.add(lib);
                            }
                        }
                        adapter.updateList(fullList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(liberary_list.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void filterList(String area) {
        List<LiberaryModel> filtered = new ArrayList<>();
        for (LiberaryModel lib : fullList) {
            if (lib.getArea().toLowerCase().contains(area.toLowerCase())) {
                filtered.add(lib);
            }
        }
        adapter.updateList(filtered);
    }
}