package com.example.android.pawstwo.NY;

public class NyFriends {

    public String date;
    public String name;
    public String image;

    public NyFriends(){


    }

    public NyFriends( String name, String image ) {
        this.name = name;
        this.image = image;
    }

    public NyFriends( String date ) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage( String image ) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate( String date ) {
        this.date = date;
    }
}
