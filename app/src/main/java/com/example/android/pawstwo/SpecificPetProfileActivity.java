package com.example.android.pawstwo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SpecificPetProfileActivity extends AppCompatActivity {


    private TextView mSpecType;
    private TextView mSpecFamily;
    private TextView mSpecDescription;
    private TextView mUploadedByUser;
    private ImageView mSpecPetPic;
    private Button mSendUserMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_specific_pet_profile );

        getIncomingIntent ();


        mSpecType = findViewById ( R.id.tv_specific_pet_type );
        mSpecFamily= findViewById ( R.id.tv_specific_pet_family );
        mSpecDescription = findViewById ( R.id.tv_specific_pet_description );
        mUploadedByUser=findViewById ( R.id.tv_specific_pet_uploadedbyuser );
        mSpecPetPic=findViewById ( R.id.iv_specific_pet_pic );
        mSendUserMessage= findViewById ( R.id.btn_send_message );

    }

    private void getIncomingIntent(){

        if (getIntent ().hasExtra ( "image_url" )&& getIntent ().hasExtra ( "pet_description" )){

            String imageUrl=getIntent ().getStringExtra ( "image_url" );
            String petDescription=getIntent ().getStringExtra ( "pet_description" );

            setPetProfile (petDescription );



        }
    }

    private void setPetProfile ( String petDescription){

        mSpecDescription = findViewById ( R.id.tv_specific_pet_description );
        mSpecDescription.setText ( petDescription );















    }
}