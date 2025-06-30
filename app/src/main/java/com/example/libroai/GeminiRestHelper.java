package com.example.libroai;

import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class GeminiRestHelper {

    private static final String API_KEY = "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";
    private static final String ENDPOINT = "https://openrouter.ai/api/v1/chat/completions";

    private final OkHttpClient client = new OkHttpClient();

    public interface ResponseCallback {
        void onResponse(String text);
    }

    public void getSuggestion(String prompt, ResponseCallback cb) {
        try {
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", prompt);

            JSONArray messagesArray = new JSONArray();
            messagesArray.put(message);

            JSONObject body = new JSONObject();
            body.put("model", "mistralai/mistral-7b-instruct"); // ✅ FREE model
            body.put("messages", messagesArray);

            RequestBody reqBody = RequestBody.create(
                    body.toString(),
                    MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(ENDPOINT)
                    .post(reqBody)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("HTTP-Referer", "https://libroai.app") // just a name, not required to exist
                    .addHeader("X-Title", "LibroAI Book Bot")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("OpenRouter", "Network error: " + e.getMessage(), e);
                    cb.onResponse("Network error: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String resBody = response.body().string();
                    Log.d("OpenRouter", "Code: " + response.code());
                    Log.d("OpenRouter", "Body: " + resBody);

                    if (response.code() != 200) {
                        cb.onResponse("Error: " + resBody);
                        return;
                    }

                    try {
                        JSONObject json = new JSONObject(resBody);
                        JSONArray choices = json.getJSONArray("choices");
                        JSONObject msg = choices.getJSONObject(0).getJSONObject("message");
                        String reply = msg.getString("content");
                        cb.onResponse(reply.trim());
                    } catch (Exception e) {
                        cb.onResponse("Parsing error: " + e.getMessage());
                    }
                }
            });
        } catch (Exception ex) {
            cb.onResponse("Error forming request.");
        }
    }
}
