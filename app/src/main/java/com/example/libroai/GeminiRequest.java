package com.example.libroai;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GeminiRequest extends AppCompatActivity {

    GeminiRestHelper geminiHelper = new GeminiRestHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Just for testing Gemini on launch
        String prompt = "Hello Gemini! What's the weather like on Mars?";
        geminiHelper.getSuggestion(prompt, new GeminiRestHelper.ResponseCallback() {
            @Override
            public void onResponse(String text) {
                runOnUiThread(() -> {
                    Toast.makeText(GeminiRequest.this, "Gemini says: " + text, Toast.LENGTH_LONG).show();
                    Log.d("GeminiResponse", text);
                });
            }
        });
    }
}
