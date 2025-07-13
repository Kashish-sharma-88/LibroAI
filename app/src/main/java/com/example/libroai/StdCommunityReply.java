package com.example.libroai;

import com.google.firebase.Timestamp;

public class StdCommunityReply {
    private String replyId;
    private String postId;
    private String userId;
    private String userName;
    private String content;
    private Timestamp timestamp;
    private boolean isAccepted;

    // Default constructor for Firestore
    public StdCommunityReply() {}

    public StdCommunityReply(String replyId, String postId, String userId, String userName, String content) {
        this.replyId = replyId;
        this.postId = postId;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.timestamp = Timestamp.now();
        this.isAccepted = false;
    }

    // Getters and Setters
    public String getReplyId() { return replyId; }
    public void setReplyId(String replyId) { this.replyId = replyId; }

    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    public boolean isAccepted() { return isAccepted; }
    public void setAccepted(boolean accepted) { isAccepted = accepted; }
} 