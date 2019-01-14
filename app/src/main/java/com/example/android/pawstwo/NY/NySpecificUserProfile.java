package com.example.android.pawstwo.NY;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pawstwo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class NySpecificUserProfile extends AppCompatActivity {

    private ImageView mProfileImage;
    private TextView mProfileName, mProfileFriendsCount;
    private Button mProfileSendRequest;

    private DatabaseReference mUsersDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_ny_specific_user_profile );


       String uid= getIntent ().getStringExtra ("userId");
       mAuth=FirebaseAuth.getInstance ();

       mUsersDatabase=FirebaseDatabase.getInstance ().getReference ().child ( "Users" ).child ( uid );

        mProfileImage = findViewById ( R.id.profile_image );
        mProfileName=findViewById ( R.id.ny_tv_specific_profile_name );
        mProfileFriendsCount=findViewById ( R.id. profile_totalFriends);
        mProfileSendRequest=findViewById ( R.id.profile_send_req_btn);


        mProgressDialog = new ProgressDialog ( this );
        mProgressDialog.setTitle ( "Loading User Data" );
        mProgressDialog.setMessage ( "Please Wait" );
        mProgressDialog.setCanceledOnTouchOutside ( false );
        mProgressDialog.show ();

        mUsersDatabase.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                String display_name=dataSnapshot.child ( "userName" ).getValue ().toString ();
                String image=dataSnapshot.child ( "imageUrl" ).getValue ().toString ();

                mProfileName.setText ( display_name );

                Picasso.with ( NySpecificUserProfile.this ).load ( image ).placeholder ( R.drawable.com_facebook_profile_picture_blank_portrait ).into(mProfileImage);

                mProgressDialog.dismiss ();




            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );


    }
}
