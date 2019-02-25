package com.example.android.pawstwo.NY;

import com.google.android.gms.maps.model.LatLng;

public class FavsTwo {


    private String mType;
    private String mFamily;
    private String mDescription;
    private String mUploader;
    private String mLat;
    private String mLong;
    private String mImageUrl;




    public FavsTwo() {
    }


    public FavsTwo( String mType, String mFamily, String mDescription, String mUploader, String mImageUrl, String mLat, String mLong ) {
        this.mType = mType;
        this.mFamily=mFamily;
        this.mDescription=mDescription;
        this.mUploader=mUploader;
        this.mLat=mLat;
        this.mLong=mLong;
        this.mImageUrl=mImageUrl;


    }


    public String getmType() {
        return mType;
    }

    public void setmType( String mType ) {
        this.mType = mType;
    }

    public String getmFamily() {
        return mFamily;
    }

    public void setmFamily( String mFamily ) {
        this.mFamily = mFamily;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription( String mDescription ) {
        this.mDescription = mDescription;
    }

    public String getmUploader() {
        return mUploader;
    }

    public void setmUploader( String mUploader ) {
        this.mUploader = mUploader;
    }

    public String getmLat() {
        return mLat;
    }

    public void setmLat( String mLat ) {
        this.mLat = mLat;
    }

    public String getmLong() {
        return mLong;
    }

    public void setmLong( String mLong ) {
        this.mLong = mLong;
    }
    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl( String mImageUrl ) {
        this.mImageUrl = mImageUrl;
    }


}
