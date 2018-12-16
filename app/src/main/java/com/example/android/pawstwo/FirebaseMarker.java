package com.example.android.pawstwo;

public class FirebaseMarker {

    public double latitude;
    public double longtitude;



    public FirebaseMarker(){

    }
    public FirebaseMarker(double latitude, double longtitude){

        this.latitude=latitude;
        this.longtitude=longtitude;


    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }



    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }


}
