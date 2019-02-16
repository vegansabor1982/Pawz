package com.example.android.pawstwo;

public class UserProfile {

    public String userName;
    public String userEmail;
    public String imageUrl;


    public UserProfile(){


    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl( String imageUrl ) {
        this.imageUrl = imageUrl;
    }

    public UserProfile( String userName, String userEmail, String imageUrl) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.imageUrl=imageUrl;


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






}