package com.example.libroai;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class AiRecommendationActivity extends AppCompatActivity {

    private static final int VOICE_INPUT_REQUEST = 100;

    private RecyclerView chatRecyclerView;
    private EditText userInput;
    private ImageView sendBtn, voiceInput;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList;
    private GeminiRestHelper restHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_recommendation);

        // Bind views
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        userInput        = findViewById(R.id.userInput);
        sendBtn          = findViewById(R.id.sendBtn);
        voiceInput       = findViewById(R.id.voiceInput);

        // RecyclerView setup
        messageList    = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, this);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(messageAdapter);

        // Initialize REST helper
        restHelper = new GeminiRestHelper();

        // Listeners
        sendBtn.setOnClickListener(v -> onSendClicked());
        voiceInput.setOnClickListener(v -> startVoiceInput());
    }

    private void onSendClicked() {
        String prompt = userInput.getText().toString().trim();
        if (prompt.isEmpty()) {
            Toast.makeText(this, "Please enter a query", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add user message bubble
        addMessage(prompt, /* isUser= */ true);
        userInput.setText("");

        // Call Gemini REST
        restHelper.getSuggestion(prompt, reply ->
                runOnUiThread(() -> addMessage(reply, /* isUser= */ false))
        );
    }

    private void addMessage(String text, boolean isUser) {
        messageList.add(new Message(text, isUser));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.scrollToPosition(messageList.size() - 1);
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        try {
            startActivityForResult(intent, VOICE_INPUT_REQUEST);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this,
                    "Voice input not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_INPUT_REQUEST
                && resultCode == RESULT_OK
                && data != null) {
            ArrayList<String> results =
                    data.getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS
                    );
            if (results != null && !results.isEmpty()) {
                userInput.setText(results.get(0));
            }
        }
    }
}
