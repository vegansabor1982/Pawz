package com.example.android.pawstwo.NY;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.pawstwo.Notifications.Data;
import com.example.android.pawstwo.PetAdapterTest;
import com.example.android.pawstwo.PetAdapterTestThree;
import com.example.android.pawstwo.PetAdapterTestTwo;
import com.example.android.pawstwo.R;
import com.example.android.pawstwo.SpecificPetProfileActivity;
import com.example.android.pawstwo.TestHomeActivity;
import com.example.android.pawstwo.UploadTest;
import com.example.android.pawstwo.UserProfile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.android.pawstwo.TestHomeActivity.EXTRA_URL;
import static com.example.android.pawstwo.TestHomeActivity.PET_DESCRIPTION;
import static com.example.android.pawstwo.TestHomeActivity.PET_FAMILY;
import static com.example.android.pawstwo.TestHomeActivity.PET_LATITUDE;
import static com.example.android.pawstwo.TestHomeActivity.PET_LONGTITUDE;
import static com.example.android.pawstwo.TestHomeActivity.PET_TYPE;
import static com.example.android.pawstwo.TestHomeActivity.UPLOADER_NAME;

public class FavouritesActivity extends AppCompatActivity implements  PetAdapterTestThree.OnItemClickListener{



    private RecyclerView mRecyclerView;
    private PetAdapterTestThree mPetAdapterTestThree;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<FavsTwo> mFavs;


    private FirebaseUser mUser;

    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase firebaseDatabase;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_favourites );







        mAuth=FirebaseAuth.getInstance ();

        mUser=FirebaseAuth.getInstance().getCurrentUser();

        String user = mUser.getUid ();


        mRecyclerView=findViewById ( R.id.list_favourites );


        mLayoutManager = new GridLayoutManager (this, 2);
        (( GridLayoutManager ) mLayoutManager).setReverseLayout ( false );
        // (( GridLayoutManager ) mLayoutManager).getReverseLayout ( true );
        mRecyclerView.setLayoutManager ( mLayoutManager );


        mDatabaseRef = FirebaseDatabase.getInstance ().getReference ().child ( "Favourites" ).child ( user );
        mDatabaseRef.keepSynced ( true );

        firebaseDatabase=FirebaseDatabase.getInstance ();

        DatabaseReference databaseReference = firebaseDatabase.getReference ().child ( "Favourites" ).child ( mAuth.getUid ());

        mFavs = new ArrayList<> ();

       // Query query = mDatabaseRef.orderByChild ( mAuth.getCurrentUser ().getUid () ).equalTo ( mAuth.getCurrentUser ().getUid () );

        databaseReference.addValueEventListener ( new ValueEventListener () {


            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                mFavs.clear ();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren ()) {
                    FavsTwo favsTwo = postSnapshot.getValue ( FavsTwo.class );

                    mFavs.add ( favsTwo );


                }


                mPetAdapterTestThree = new PetAdapterTestThree ( FavouritesActivity.this, mFavs );
                mRecyclerView.setAdapter ( mPetAdapterTestThree );

                mPetAdapterTestThree.setOnItemClickListener ( FavouritesActivity.this );





            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );




    }



    @Override
    public void onItemClick( final int position ) {

        CharSequence options[] = new CharSequence[]{"Go to Submission", "Remove from Favourites"};

        AlertDialog.Builder builder = new AlertDialog.Builder ( FavouritesActivity.this );

        builder.setTitle ( "Select Option" );

        builder.setItems ( options, new DialogInterface.OnClickListener () {
            @Override
            public void onClick( DialogInterface dialogInterface, int i ) {

                if (i == 0) {

                    Intent d = new Intent ( FavouritesActivity.this, SpecificPetProfileActivity.class );

                    FavsTwo clickedItem = mFavs.get ( position );

                    d.putExtra ( EXTRA_URL, clickedItem.getmImageUrl () );
                    d.putExtra ( PET_TYPE, clickedItem.getmType () );
                    d.putExtra ( PET_FAMILY, clickedItem.getmFamily () );
                    d.putExtra ( PET_DESCRIPTION, clickedItem.getmDescription () );
                    d.putExtra ( UPLOADER_NAME, clickedItem.getmUploader () );
                    d.putExtra ( PET_LATITUDE, clickedItem.getmLat () );
                    d.putExtra ( PET_LONGTITUDE, clickedItem.getmLong () );


                    startActivity ( d );

                }

                if (i == 1) {



                }


            }

        } );
        builder.show ();







    }

    @Override
    public void onPointerCaptureChanged( boolean hasCapture ) {

    }




}
