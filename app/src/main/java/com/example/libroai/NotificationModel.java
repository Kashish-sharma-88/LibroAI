package com.example.libroai;

public class NotificationModel {
    private String message;
    private String senderId;
    private String type;
    private String timestamp;
    private boolean isRead;

    public NotificationModel() {}

    public NotificationModel(String message, String senderId, String type, String timestamp, boolean isRead) {
        this.message = message;
        this.senderId = senderId;
        this.type = type;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
