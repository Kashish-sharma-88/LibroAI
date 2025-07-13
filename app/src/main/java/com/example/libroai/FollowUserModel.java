package com.example.libroai;
public class FollowUserModel {
    private String userId;
    private String userName;
    private String userEmail;
    private String userType;
    private String userBio;
    private String libraryName;
    private boolean isFollowing;
    private int followersCount;

    public FollowUserModel() {}

    // Getter-Setter

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserBio() {
        return userBio;
    }
    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getLibraryName() {
        return libraryName;
    }
    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public boolean isFollowing() {
        return isFollowing;
    }
    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public int getFollowersCount() {
        return followersCount;
    }
    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }
}
