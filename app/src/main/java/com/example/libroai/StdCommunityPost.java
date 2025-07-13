package com.example.libroai;

import com.google.firebase.Timestamp;

public class StdCommunityPost {
    private String postId;
    private String userId;
    private String userName;
    private String title;
    private String content;
    private String category;
    private Timestamp timestamp;
    private int replyCount;
    private boolean isResolved;
    private String question; // Added missing field
    private boolean reported; // Added missing field
    private String userEmail; // Added missing field

    // Default constructor for Firestore
    public StdCommunityPost() {}

    public StdCommunityPost(String postId, String userId, String userName, String title, String content, String category) {
        this.postId = postId;
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.category = category;
        this.timestamp = Timestamp.now();
        this.replyCount = 0;
        this.isResolved = false;
        this.question = title; // Set question to title by default
        this.reported = false; // Set reported to false by default
        this.userEmail = ""; // Set userEmail to empty by default
    }

    // Getters and Setters
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    public int getReplyCount() { return replyCount; }
    public void setReplyCount(int replyCount) { this.replyCount = replyCount; }

    public boolean isResolved() { return isResolved; }
    public void setResolved(boolean resolved) { isResolved = resolved; }

    // Added getters and setters for missing fields
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public boolean isReported() { return reported; }
    public void setReported(boolean reported) { this.reported = reported; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
} 