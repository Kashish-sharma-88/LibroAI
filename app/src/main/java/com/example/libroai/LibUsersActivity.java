package com.example.libroai;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class LibUsersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libusers);

        LinearLayout usersList = findViewById(R.id.users_list_container);
        // Sample data
        String[][] users = {
            {"Aman Sharma", "aman@email.com"},
            {"Priya Singh", "priya@email.com"},
            {"Rahul Verma", "rahul@email.com"}
        };
        for (String[] user : users) {
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.HORIZONTAL);
            card.setBackgroundResource(android.R.color.transparent);
            card.setPadding(24, 24, 24, 24);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 36);
            card.setLayoutParams(cardParams);
            card.setBackgroundResource(android.R.color.transparent);

            // CardView-like background
            card.setBackgroundResource(android.R.color.transparent);
            card.setBackground(getResources().getDrawable(R.drawable.bg_user_card));

            // Icon
            ImageView icon = new ImageView(this);
            icon.setImageResource(R.drawable.user);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(100, 100);
            iconParams.setMargins(0, 0, 32, 0);
            icon.setLayoutParams(iconParams);
            card.addView(icon);

            // Name and email vertical layout
            LinearLayout textLayout = new LinearLayout(this);
            textLayout.setOrientation(LinearLayout.VERTICAL);
            textLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView nameView = new TextView(this);
            nameView.setText(user[0]);
            nameView.setTextSize(20);
            nameView.setTypeface(null, android.graphics.Typeface.BOLD);
            nameView.setTextColor(0xFF4B2C20);

            TextView emailView = new TextView(this);
            emailView.setText(user[1]);
            emailView.setTextSize(14);
            emailView.setTextColor(0xFFBCAAA4);

            textLayout.addView(nameView);
            textLayout.addView(emailView);
            card.addView(textLayout);

            usersList.addView(card);
        }
    }
} 