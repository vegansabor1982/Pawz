package com.example.android.pawstwo;

import android.widget.ImageView;

public class UserProfile {

    public String userName;
    public String userEmail;


    public UserProfile(){


    }

    public UserProfile(String userName, String userEmail, ImageView profilePic) {
        this.userName = userName;
        this.userEmail = userEmail;

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
