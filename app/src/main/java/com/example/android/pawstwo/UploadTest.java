package com.example.android.pawstwo;

public class UploadTest {

    private String mType;
    private String mFamily;
    private String mDescription;
    private String mImageUrl;


    public UploadTest (){


    }

    public UploadTest( String type, String family, String description, String imageurl ){

        if (description.trim ().equals ( "" )){
            description="No Description";
        }

        mType=type;
        mFamily= family;
        mDescription=description;
        mImageUrl=imageurl;


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


}

