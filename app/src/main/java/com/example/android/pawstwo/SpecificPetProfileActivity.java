package com.example.android.pawstwo;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.example.android.pawstwo.TestHomeActivity.EXTRA_URL;
import static com.example.android.pawstwo.TestHomeActivity.PET_DESCRIPTION;
import static com.example.android.pawstwo.TestHomeActivity.PET_FAMILY;
import static com.example.android.pawstwo.TestHomeActivity.PET_TYPE;

public class SpecificPetProfileActivity extends AppCompatActivity {


    private TextView mSpecType;
    private TextView mSpecFamily;
    private TextView mSpecDescription;
    private TextView mUploadedByUser;
    private ImageView mSpecPetPic;
    private Button mSendUserMessage;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private MapView mMapView;

    public double latitude;
    public double longtitude;



    private Glide mGlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_specific_pet_profile );




        mSpecType = findViewById ( R.id.tv_specific_pet_type );
        mSpecFamily= findViewById ( R.id.tv_specific_pet_family );
        mSpecDescription = findViewById ( R.id.tv_specific_pet_description );
        mUploadedByUser=findViewById ( R.id.tv_specific_pet_uploadedbyuser );
        mSpecPetPic=findViewById ( R.id.iv_specific_pet_pic );
        mSendUserMessage= findViewById ( R.id.btn_send_message );
        mMapView=findViewById ( R.id.mapView );



        Intent r =getIntent ();
        String imageUrl= getIntent ().getStringExtra (EXTRA_URL);
        String petType=getIntent ().getStringExtra ( PET_TYPE );
        String petFamily =getIntent ().getStringExtra ( PET_FAMILY );
        String petDescription=getIntent ().getStringExtra ( PET_DESCRIPTION );

        Picasso.get ().load ( imageUrl ).fit().centerCrop().into (mSpecPetPic  );
        mSpecType.setText(petType);
        mSpecFamily.setText ( petFamily );
        mSpecDescription.setText ( petDescription );

        mDatabase=FirebaseDatabase.getInstance ();
        mAuth=FirebaseAuth.getInstance ();


        DatabaseReference mRef = mDatabase.getReference ().child("Users").child ( mAuth.getUid () );














    }

















}