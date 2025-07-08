package com.example.libroai;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private ViewPager2 imageSlider;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private ImageView menuButton, profileButton;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;

    private LinearLayout tabFree, tabPaid, tabTrending;
    private TextView tabFreeText, tabPaidText, tabTrendingText;
    private RecyclerView topBooksRecycler;
    private bookAdapter freeAdapter, paidAdapter, trendingAdapter;

    FloatingActionButton chatbotButton;

    private final int[] sliderImages = { R.drawable.img1, R.drawable.img2, R.drawable.img3 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        /* ===== find views ===== */
        imageSlider   = findViewById(R.id.imageSlider);
        menuButton    = findViewById(R.id.menu_button);
        profileButton = findViewById(R.id.profile_button);

        drawerLayout   = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        tabFree     = findViewById(R.id.tab_top_free);
        tabPaid     = findViewById(R.id.tab_top_paid);
        tabTrending = findViewById(R.id.tab_trending);
        chatbotButton = findViewById(R.id.chatbotButton);

        // second child in each tab layout is the TextView label
        tabFreeText     = (TextView) tabFree.getChildAt(1);
        tabPaidText     = (TextView) tabPaid.getChildAt(1);
        tabTrendingText = (TextView) tabTrending.getChildAt(1);

        topBooksRecycler = findViewById(R.id.top_books_recycler);

        /* ===== Slider setup ===== */
        imageSlider.setAdapter(new SliderAdapter(sliderImages));
        imageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 5000);
            }
        });

        /* ===== Drawer & profile ===== */
        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        profileButton.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

        navigationView.setNavigationItemSelectedListener(item -> {
            handleDrawerItem(item);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        chatbotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this,AiRecommendationActivity.class);
                startActivity(intent);
            }
        });

        /* ===== Bottom navigation ===== */
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if      (id == R.id.nav_home)      return true;
            else if (id == R.id.nav_doubts)    { startActivity(new Intent(this, DoubtsActivity.class));    return true; }
            else if (id == R.id.nav_follow)    { startActivity(new Intent(this, StdFollowActivity.class));    return true; }
            else if (id == R.id.nav_library)   { startActivity(new Intent(this, LibraryListActivity.class)); return true; }
            else if (id == R.id.nav_forums)    { startActivity(new Intent(this, DiscussionsForumsActivity.class)); return true; }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        /* ===== Top‑books RecyclerView ===== */
        topBooksRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        freeAdapter     = new bookAdapter(sampleTopFree());
        paidAdapter     = new bookAdapter(sampleTopPaid());
        trendingAdapter = new bookAdapter(sampleTrending());

        topBooksRecycler.setAdapter(freeAdapter);   // default view
        highlightTab(0);

        /* ===== Tab clicks ===== */
        tabFree.setOnClickListener(v -> switchTopList(0));
        tabPaid.setOnClickListener(v -> switchTopList(1));
        tabTrending.setOnClickListener(v -> switchTopList(2));
    }

    /* ---------- Tab helpers ---------- */
    private void switchTopList(int index) {
        if (index == 0)       topBooksRecycler.setAdapter(freeAdapter);
        else if (index == 1)  topBooksRecycler.setAdapter(paidAdapter);
        else                  topBooksRecycler.setAdapter(trendingAdapter);
        highlightTab(index);
    }
    private void highlightTab(int index) {
        int highlight = ContextCompat.getColor(this, R.color.brown_primary);
        int normal    = ContextCompat.getColor(this, android.R.color.black);

        tabFreeText.setTextColor(index == 0 ? highlight : normal);
        tabPaidText.setTextColor(index == 1 ? highlight : normal);
        tabTrendingText.setTextColor(index == 2 ? highlight : normal);
    }

    /* ---------- Sample data ---------- */
    private List<Book> sampleTopFree() {
        List<Book> list = new ArrayList<>();
        list.add(new Book("Free Book 1", "Author A", "Fiction",  "Free"));
        list.add(new Book("Free Book 2", "Author B", "Fantasy",  "Free"));
        list.add(new Book("Free Book 3", "Author C", "Romance",  "Free"));
        return list;
    }
    private List<Book> sampleTopPaid() {
        List<Book> list = new ArrayList<>();
        list.add(new Book("Paid Book 1", "Author D", "Sci‑Fi",  "$3.99"));
        list.add(new Book("Paid Book 2", "Author E", "History", "$2.49"));
        return list;
    }
    private List<Book> sampleTrending() {
        List<Book> list = new ArrayList<>();
        list.add(new Book("Trending 1", "Author F", "Mystery",    "Trending"));
        list.add(new Book("Trending 2", "Author G", "Adventure",  "Trending"));
        return list;
    }

    /* ---------- Slider auto‑scroll ---------- */
    private final Runnable sliderRunnable = () -> {
        int next = (imageSlider.getCurrentItem() + 1) % sliderImages.length;
        imageSlider.setCurrentItem(next, true);
    };
    @Override protected void onPause()  { super.onPause(); sliderHandler.removeCallbacks(sliderRunnable); }
    @Override protected void onResume() { super.onResume(); sliderHandler.postDelayed(sliderRunnable, 5000); }

    /* ---------- Drawer navigation ---------- */
    private void handleDrawerItem(@NonNull MenuItem item) {
        int id = item.getItemId();
        if      (id == R.id.available_books)  startActivity(new Intent(this, AvailableBooksActivity.class));
        else if (id == R.id.issued_books)     startActivity(new Intent(this, IssuedBooksActivity.class));
        else if (id == R.id.notifications)    startActivity(new Intent(this, NotificationsActivity.class));
        else if (id == R.id.bookmarks)        startActivity(new Intent(this, BookmarksActivity.class));
        else if (id == R.id.map)        startActivity(new Intent(this, MapActivity.class));
        else if (id == R.id.library_requests) startActivity(new Intent(this, StdRequestStatusActivity.class));
        else if (id == R.id.notes)            startActivity(new Intent(this, NotesActivity.class));
        else if (id == R.id.feedback)         startActivity(new Intent(this, FeedbackActivity.class));
        else if (id == R.id.settings)         startActivity(new Intent(this, SettingsActivity.class));
    }
}
