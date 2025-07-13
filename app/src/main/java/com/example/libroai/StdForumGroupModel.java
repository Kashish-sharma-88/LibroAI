package com.example.libroai;

public class StdForumGroupModel {
    private String groupId;
    private String groupName;
    private String groupDescription;
    private String adminId;
    private String adminName;
    private int memberCount;
    private String category;
    private String createdAt;
    private boolean isPrivate;

    // Default constructor for Firebase
    public StdForumGroupModel() {
    }

    public StdForumGroupModel(String groupId, String groupName, String groupDescription, 
                             String adminId, String adminName, String category) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.adminId = adminId;
        this.adminName = adminName;
        this.category = category;
        this.memberCount = 1; // Admin is the first member
        this.createdAt = String.valueOf(System.currentTimeMillis());
        this.isPrivate = false;
    }

    // Getters and Setters
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
} 