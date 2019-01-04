package com.example.android.pawstwo;

public class UploadTest {

    private String mType;
    private String mFamily;
    private String mDescription;
    private String mImageUrl;
    private String mLat;
    private String mLongt;
    private String mUploader;
    private  String mUploaderName;




    public UploadTest (){


    }

    public UploadTest( String type, String family, String description, String imageurl, String latitude, String longtitude, String uploader, String uploaderName ){

        if (description.trim ().equals ( "" )){
            description="No Description";
        }


        mType=type;
        mFamily= family;
        mDescription=description;
        mImageUrl=imageurl;
        mLat=latitude;
        mLongt=longtitude;
        mUploader=uploader;
        mUploaderName=uploaderName;



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

    public String getmUploader() {
        return mUploader;
    }

    public void setmUploader( String mUploader ) {
        this.mUploader = mUploader;
    }

    public String getmUploaderName() {
        return mUploaderName;
    }

    public void setmUploaderName( String mUploaderName ) {
        this.mUploaderName = mUploaderName;
    }
}