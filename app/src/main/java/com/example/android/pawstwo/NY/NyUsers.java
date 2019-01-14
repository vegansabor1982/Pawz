package com.example.android.pawstwo.NY;

public class NyUsers {


    public String userName;
    public String imageUrl;
    public String thumb_image;


    public NyUsers() {
    }



    public NyUsers( String userName, String image, String thumb_image ) {
        this.userName = userName;
        this.imageUrl = image;
        this.thumb_image=thumb_image;



    }

    public String getUserName() {
        return userName;
    }

    public void setUserName( String userName ) {
        this.userName = userName;
    }

    public String getImage() {
        return imageUrl;
    }

    public void setImage( String image ) {
        this.imageUrl = image;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image( String thumb_image ) {
        this.thumb_image = thumb_image;
    }


}
