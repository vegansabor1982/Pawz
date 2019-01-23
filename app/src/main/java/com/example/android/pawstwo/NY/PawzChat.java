package com.example.android.pawstwo.NY;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class PawzChat  extends Application {

    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate() {
        super.onCreate ();

        FirebaseDatabase.getInstance ().setPersistenceEnabled ( true );

        mAuth = FirebaseAuth.getInstance ();
//       mUserDatabase = FirebaseDatabase.getInstance ().getReference ().child ( "Users" ).child ( mAuth.getCurrentUser ().getUid () );

/*  mUserDatabase.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                if (dataSnapshot != null) {


                    mUserDatabase.child ( "online" ).onDisconnect ().setValue ( false );
                    mUserDatabase.child ( "online" ).setValue ( true );


                }


            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );*/
    }
}
