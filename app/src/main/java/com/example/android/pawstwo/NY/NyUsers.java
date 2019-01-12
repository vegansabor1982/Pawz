package com.example.android.pawstwo.NY;

public class NyUsers {


    public String userName;
    public String image;

    public NyUsers() {
    }

    public NyUsers( String userName, String image ) {
        this.userName = userName;
        this.image = image;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName( String userName ) {
        this.userName = userName;
    }

    public String getImage() {
        return image;
    }

    public void setImage( String image ) {
        this.image = image;
    }
}
