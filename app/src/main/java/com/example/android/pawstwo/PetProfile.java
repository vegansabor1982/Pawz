package com.example.android.pawstwo;

public class PetProfile {

    public String description;
    public String spinner;
    public String spinner2;


    /*public PetProfile(String description, String family, String type, ImageView petPic) {


    }*/
    public PetProfile(String type, String family, String description) {
        this.description = description;
        this.spinner = type;
        this.spinner2 = family;



    }

    public String getDescription() {
        return description;
    }

    public String getSpinner() {
        return spinner;
    }

    public String getSpinner2() {
        return spinner2;
    }
}


    //this is to check update





