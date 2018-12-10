package com.example.android.pawstwo;

import android.widget.ImageView;

public class PetProfile {

    public String Description;
    public String spinner;
    public String spinner2;

    public PetProfile(String description, String family, String type, ImageView petPic) {


    }

    public PetProfile ( String type, String family, String description ) {
            this.Description = description;
            this.spinner = type;
            this.spinner2 = family;


        }

    public String getPetFamily() {
        return spinner2;
    }

    public String getPetType() {
        return spinner;
    }
    public String getPetDescription() {
        return Description;
    }
    }



    

