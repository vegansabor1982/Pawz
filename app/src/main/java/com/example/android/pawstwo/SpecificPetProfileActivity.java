package com.example.android.pawstwo;

import android.content.Intent;
import android.location.Location;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.android.pawstwo.NY.ChatRoomActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.example.android.pawstwo.TestHomeActivity.EXTRA_URL;
import static com.example.android.pawstwo.TestHomeActivity.PET_DESCRIPTION;
import static com.example.android.pawstwo.TestHomeActivity.PET_FAMILY;
import static com.example.android.pawstwo.TestHomeActivity.PET_LATITUDE;
import static com.example.android.pawstwo.TestHomeActivity.PET_LONGTITUDE;
import static com.example.android.pawstwo.TestHomeActivity.PET_TYPE;
import static com.example.android.pawstwo.TestHomeActivity.UPLOADER_NAME;

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

    private GoogleMap mMapTwo;

    private LatLng mCoords;



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






        Intent r =getIntent ();
        String imageUrl= getIntent ().getStringExtra (EXTRA_URL);
        String petType=getIntent ().getStringExtra ( PET_TYPE );
        String petFamily =getIntent ().getStringExtra ( PET_FAMILY );
        String petDescription=getIntent ().getStringExtra ( PET_DESCRIPTION );
        final String uploaderName=getIntent ().getStringExtra ( UPLOADER_NAME );



        Picasso.with (getBaseContext ()).load ( imageUrl ).fit().centerCrop().into (mSpecPetPic  );
        mSpecType.setText(petType);
        mSpecFamily.setText ( petFamily );
        mSpecDescription.setText ( petDescription );
        mUploadedByUser.setText ( "Uploaded by: "+ uploaderName );





        mDatabase=FirebaseDatabase.getInstance ();
        mAuth=FirebaseAuth.getInstance ();


        DatabaseReference mRef = mDatabase.getReference ().child("Users").child ( mAuth.getUid () );


        mSendUserMessage=findViewById ( R.id.btn_send_message );



        mSendUserMessage.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View view ) {




                Intent u = new Intent ( SpecificPetProfileActivity.this, ChatRoomActivity.class );
                startActivity ( u );


            }
        } );





















    }


}