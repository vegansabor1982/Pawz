package com.example.android.pawstwo;

public class UploadTest {

    private String mType;
    private String mFamily;
    private String mDescription;
    private String mImageUrl;
    private String mLat;
    private String mLongt;



    public UploadTest (){


    }

    public UploadTest( String type, String family, String description, String imageurl, String latitude, String longtitude ){

        if (description.trim ().equals ( "" )){
            description="No Description";
        }


        mType=type;
        mFamily= family;
        mDescription=description;
        mImageUrl=imageurl;
        mLat=latitude;
        mLongt=longtitude;


    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmFamily() {
        return mFamily;
    }

    public void setmFamily(String mFamily) {
        this.mFamily = mFamily;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmLat() {
        return mLat;
    }

    public void setmLat( String mLat ) {
        this.mLat = mLat;
    }

    public String getmLongt() {
        return mLongt;
    }

    public void setmLongt( String mLongt ) {
        this.mLongt = mLongt;
    }
}

