package com.example.libroai;

public class StdUserModel {
    
    private String userId;
    private String userName;
    private String userEmail;
    private String userType; // "student" or "librarian"
    private String userImage;
    private String userBio;
    private String libraryName; // for librarians
    private boolean isFollowing;
    private int followersCount;
    private int followingCount;

    // Required empty constructor for Firebase
    public StdUserModel() {
    }

    // Parameterized constructor
    public StdUserModel(String userId, String userName, String userEmail, String userType, 
                       String userImage, String userBio, String libraryName, 
                       boolean isFollowing, int followersCount, int followingCount) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userType = userType;
        this.userImage = userImage;
        this.userBio = userBio;
        this.libraryName = libraryName;
        this.isFollowing = isFollowing;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

    // Getter methods
    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserBio() {
        return userBio;
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

    // Setter methods
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
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
} 