package com.example.libroai;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FollowRequestHelper {

    public static void sendFollowRequest(String senderId, String receiverId, String senderName, String senderEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // ✅ Check if receiver exists in "users"
        db.collection("users")
                .document(receiverId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Log.d("FollowRequest", "✅ Receiver exists: " + receiverId);

                        // ✅ Prepare follow request data
                        Map<String, Object> requestData = new HashMap<>();
                        requestData.put("senderId", senderId);
                        requestData.put("senderName", senderName);
                        requestData.put("senderEmail", senderEmail);
                        requestData.put("timestamp", System.currentTimeMillis());

                        // ✅ Add request to receiver's followRequests subcollection
                        db.collection("users")
                                .document(receiverId)
                                .collection("followRequests")
                                .document(senderId)
                                .set(requestData)
                                .addOnSuccessListener(unused -> {
                                    Log.d("FollowRequest", "📨 Follow request sent to " + receiverId);

                                    // ✅ Send in-app notification
                                    NotificationHelper.sendNotification(
                                            receiverId,
                                            "You have a follow request from " + senderName,
                                            senderId,
                                            "follow_request"
                                    );
                                })
                                .addOnFailureListener(e ->
                                        Log.e("FollowRequest", "❌ Failed to send follow request", e));

                    } else {
                        Log.e("FollowRequest", "❌ Receiver not found: " + receiverId);
                    }
                })
                .addOnFailureListener(e ->
                        Log.e("FollowRequest", "❌ Error checking receiver existence", e));
    }
}
