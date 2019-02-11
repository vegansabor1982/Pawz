package com.example.android.pawstwo.NY;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.android.pawstwo.PetAdapterTestTwo;
import com.example.android.pawstwo.R;
import com.example.android.pawstwo.SpecificPetProfileActivity;
import com.example.android.pawstwo.UploadTest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoundOnlyActicity extends AppCompatActivity implements  PetAdapterTestTwo.OnItemClickListener{


    private RecyclerView mRecyclerView;
    private PetAdapterTestTwo mPetAdapterTestTwo;
    private Query mDatabaseRef;
    private List<UploadTest> mUploads;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth firebaseAuth;

    int number = 0;


    public static final String EXTRA_URL = "imageUrl";
    public static final String PET_TYPE = "pet_type";
    public static final String PET_FAMILY = "pet_family";
    public static final String PET_DESCRIPTION = "pet_description";
    public static final String PET_LATITUDE = "pet_latitude";
    public static final String PET_LONGTITUDE = "pet_longtitude";
    public static final String UPLOADER_NAME = "uploader_name";

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_found_only_acticity );


        mRecyclerView = findViewById ( R.id.list_found_only );

        mRecyclerView.setHasFixedSize ( true );

        mLayoutManager = new GridLayoutManager (this, 2);
        (( GridLayoutManager ) mLayoutManager).setReverseLayout ( false );
        // (( GridLayoutManager ) mLayoutManager).getReverseLayout ( true );
        mRecyclerView.setLayoutManager ( mLayoutManager );




        mDatabaseRef = FirebaseDatabase.getInstance ().getReference ().child ( "Uploads" );

        mUploads = new ArrayList<> ();


        Query query = mDatabaseRef.orderByChild ( "mType" ).equalTo ( "Found" );

        query.addChildEventListener ( new ChildEventListener () {
            @Override
            public void onChildAdded( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {

                Map<String, Object> newList = (Map<String, Object>)dataSnapshot.getValue ();
                UploadTest uploadTest = dataSnapshot.getValue ( UploadTest.class );

                mUploads.add ( uploadTest );

                mPetAdapterTestTwo = new PetAdapterTestTwo ( FoundOnlyActicity.this, mUploads );
                mRecyclerView.setAdapter ( mPetAdapterTestTwo );

                mPetAdapterTestTwo.setOnItemClickListener ( FoundOnlyActicity.this );


            }

            @Override
            public void onChildChanged( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {

            }

            @Override
            public void onChildRemoved( @NonNull DataSnapshot dataSnapshot ) {

            }

            @Override
            public void onChildMoved( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {

            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

                Toast.makeText ( FoundOnlyActicity.this, databaseError.getMessage (), Toast.LENGTH_SHORT ).show ();

            }
        } );

    }

    @Override
    public void onItemClick( int position ) {

        Intent d = new Intent ( this, SpecificPetProfileActivity.class );

        UploadTest clickedItem = mUploads.get ( position );

        d.putExtra ( EXTRA_URL, clickedItem.getmImageUrl () );
        d.putExtra ( PET_TYPE, clickedItem.getmType () );
        d.putExtra ( PET_FAMILY, clickedItem.getmFamily () );
        d.putExtra ( PET_DESCRIPTION, clickedItem.getmDescription () );
        d.putExtra ( PET_LATITUDE, clickedItem.getmLat () );
        d.putExtra ( PET_LONGTITUDE, clickedItem.getmLongt () );
        d.putExtra ( UPLOADER_NAME, clickedItem.getmUploaderName () );


        startActivity ( d );

    }
}
