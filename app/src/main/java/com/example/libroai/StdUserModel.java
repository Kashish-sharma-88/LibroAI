package com.example.libroai;

public class StdUserModel {

    private String userId;
    private String name;
    private String email;
    private String role;
    private String userImage;
    private String bio;
    private String libraryName;
    private boolean isFollowing;
    private int followersCount;
    private int followingCount;

    // Required empty constructor
    public StdUserModel() {}

    public StdUserModel(String userId, String name, String email, String role,
                        String userImage, String bio, String libraryName,
                        boolean isFollowing, int followersCount, int followingCount) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.userImage = userImage;
        this.bio = bio;
        this.libraryName = libraryName;
        this.isFollowing = isFollowing;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

    // Firestore mapped fields
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getBio() {
        return bio;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    // Adapter Compatibility 👇
    public String getUserName() {
        return name; // Match your adapter
    }

    public String getUserEmail() {
        return email; // Match your adapter
    }

    public String getUserType() {
        return role; // Match your adapter
    }

    public String getUserBio() {
        return bio; // Match your adapter
    }
}
