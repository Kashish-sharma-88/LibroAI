package com.example.libroai;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NotificationHelper {

    public static void sendNotification(String receiverId, String message, String senderId, String type) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> notification = new HashMap<>();
        notification.put("message", message);
        notification.put("senderId", senderId);
        notification.put("type", type); // e.g., "request_accepted", "request_rejected", "follow_request"
        notification.put("timestamp", Timestamp.now());
        notification.put("isRead", false);

        db.collection("notifications")
                .document(receiverId)
                .collection("items")  // 🔁 Changed path for better scalability
                .add(notification);
    }
}
