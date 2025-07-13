package com.example.libroai;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class Librarian_Dasboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    private CardView cardAddLibrary, cardDelivery, cardAddBook,cardmanagebooks, libAcceptRequestsCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_dashboard);

        // Initialize Views
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        cardmanagebooks = findViewById(R.id.manage);
        cardDelivery = findViewById(R.id.card_delivery);
        cardAddBook = findViewById(R.id.add_books); // Make sure this ID exists in XML
        libAcceptRequestsCard = findViewById(R.id.lib_accept_requests_card);
        bottomNavigationView = findViewById(R.id.bottom_navigation);


        // Setup Toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Library Admin");
        }

        // Setup Navigation Drawer Toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle Navigation Item Clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_dashboard) {
                    startActivity(new Intent(Librarian_Dasboard.this, Librarian_Dasboard.class));
                    finish();
                } else if (id == R.id.nav_books) {
                    startActivity(new Intent(Librarian_Dasboard.this, book_list.class));
                } else if (id == R.id.nav_users) {
                    startActivity(new Intent(Librarian_Dasboard.this, LibUsersActivity.class));
                }else if (id== R.id.nav_orders){
                    startActivity(new Intent(Librarian_Dasboard.this, LibrarianOrdersActivity.class));
                } else if (id == R.id.nav_logout) {
                    showLogoutDialog();
                }


                drawerLayout.closeDrawers();
                return true;
            }
        });

        // Handle Dashboard Card Clicks
        cardmanagebooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Librarian_Dasboard.this, manage_books.class));
            }
        });

        cardAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Librarian_Dasboard.this, liberay_book.class));
            }
        });

        libAcceptRequestsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Librarian_Dasboard.this, JoinRequestsActivity.class));
            }
        });
        CardView cardIssuedBookRequests = findViewById(R.id.card_issued_book_requests);
        cardIssuedBookRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Librarian_Dasboard.this, libIssuedBookRequestsActivity.class));
            }
        });

        // Add click listener for records card
        CardView cardRecords = findViewById(R.id.card_records);
        if (cardRecords != null) {
            cardRecords.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.util.Log.d("LibrarianDashboard", "Records card clicked - launching LibStudentRecordsActivity");
                    try {
                        Intent intent = new Intent(Librarian_Dasboard.this, LibStudentRecordsActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        android.util.Log.e("LibrarianDashboard", "Error launching activity: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } else {
            android.util.Log.e("LibrarianDashboard", "card_records not found!");
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(Librarian_Dasboard.this, MapActivity.class));
                    return true;
                } else if (id == R.id.nav_book) {
                    startActivity(new Intent(Librarian_Dasboard.this, book_list.class));
                    return true;
                } else if (id == R.id.nav_libprofile) {
                    startActivity(new Intent(Librarian_Dasboard.this, libprofile.class));
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void showLogoutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(false);

        Button btnNo = dialog.findViewById(R.id.btn_no);
        Button btnYes = dialog.findViewById(R.id.btn_yes);

        btnNo.setOnClickListener(v -> dialog.dismiss());
        btnYes.setOnClickListener(v -> {
            Intent intent = new Intent(Librarian_Dasboard.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        dialog.show();

    }
 }

