package com.example.libroai;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import com.example.libroai.BuildConfig;


public class GeminiRestHelper {

    private static final String ENDPOINT = "https://openrouter.ai/api/v1/chat/completions";

    private final OkHttpClient client = new OkHttpClient();

    public interface ResponseCallback {
        void onResponse(String text);
    }

    public void getSuggestion(String prompt, ResponseCallback cb) {
        try {
            JSONObject json = new JSONObject();
            json.put("model", "mistralai/mistral-7b-instruct");
            JSONArray messages = new JSONArray();
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.put(userMessage);
            json.put("messages", messages);

            RequestBody requestBody = RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(ENDPOINT)
                    .post(requestBody)
                    .addHeader("Authorization", "Bearer " + BuildConfig.OPENROUTER_API_KEY) // 🔐 from gradle
                    .addHeader("HTTP-Referer", "https://libroai.example")
                    .addHeader("X-Title", "LibroAI")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, IOException e) {
                    Log.e("OpenRouter", "Request failed: " + e.getMessage());
                    cb.onResponse("Failed to connect: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res = response.body().string();
                    Log.d("OpenRouter", "Response Code: " + response.code());
                    Log.d("OpenRouter", "Response Body: " + res);

                    if (response.code() != 200) {
                        cb.onResponse("Error " + response.code() + ": " + res);
                        return;
                    }

                    try {
                        JSONObject resObj = new JSONObject(res);
                        JSONArray choices = resObj.getJSONArray("choices");
                        String reply = choices.getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");
                        cb.onResponse(reply);
                    } catch (Exception e) {
                        cb.onResponse("Parsing error: " + e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            cb.onResponse("Error preparing request: " + e.getMessage());
        }
    }
}
