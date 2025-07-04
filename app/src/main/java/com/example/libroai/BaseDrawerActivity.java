package com.example.libroai;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public abstract class BaseDrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    protected void setContent(@LayoutRes int layoutResId,
                              String title,
                              int checkedDrawerId) {

        setContentView(R.layout.activity_drawer_base);

        getLayoutInflater()
                .inflate(layoutResId, findViewById(R.id.content_frame), true);

        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(title);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                        R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = findViewById(R.id.nav_view);
        if (checkedDrawerId != 0) navView.setCheckedItem(checkedDrawerId);

        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == checkedDrawerId) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            Intent intent = null;

            if (id == R.id.available_books) intent = new Intent(this, AvailableBooksActivity.class);
            else if (id == R.id.issued_books) intent = new Intent(this, IssuedBooksActivity.class);
            else if (id == R.id.notifications) intent = new Intent(this, NotificationsActivity.class);
            else if (id == R.id.bookmarks) intent = new Intent(this, BookmarksActivity.class);
            else if (id == R.id.notes) intent = new Intent(this, NotesActivity.class);
            else if (id == R.id.feedback) intent = new Intent(this, FeedbackActivity.class);
            else if (id == R.id.settings) intent = new Intent(this, SettingsActivity.class);

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
