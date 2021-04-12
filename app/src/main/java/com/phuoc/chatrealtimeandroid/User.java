package com.phuoc.chatrealtimeandroid;

public class User {
    private String userName;
    private String email;
    private String imgProfile;

    public User(String userName, String email, String imgProfile) {
        this.userName = userName;
        this.email = email;
        this.imgProfile = imgProfile;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
    }
}
