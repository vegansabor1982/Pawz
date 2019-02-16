package com.example.android.pawstwo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView profileName;
    private TextView profileEmail;
    private Button profileUpdate;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    private Button mChangeImageBtn;
    private static final int GALLERY_PICK = 1;

    private ProgressDialog mProgressDialog;
    private StorageReference mImageStorage;
    private FirebaseUser mCurrentUser;


    private DatabaseReference mUserDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_profile );


        profileEmail = (TextView) findViewById ( R.id.tvProfileEmail );
        profileName = (TextView) findViewById ( R.id.tvProfileName );
        profilePic=(ImageView ) findViewById ( R.id.ivProfilePic );
        mChangeImageBtn= findViewById ( R.id.change_profile_image );





        firebaseAuth=FirebaseAuth.getInstance ();
        firebaseDatabase=FirebaseDatabase.getInstance ();
        firebaseStorage=FirebaseStorage.getInstance ();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true);




        DatabaseReference databaseReference = firebaseDatabase.getReference().child( "Users").child (firebaseAuth.getUid ());


        StorageReference storageReference = firebaseStorage.getReference ();


        storageReference.child ( firebaseAuth.getUid () ).child ( "Images/Profile Pic" ).getDownloadUrl ().addOnSuccessListener ( new OnSuccessListener<Uri> () {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with (getBaseContext ()).load ( uri ).fit().centerCrop().into (profilePic  );




            }
        } );



        databaseReference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile =dataSnapshot.getValue (UserProfile.class);
                profileName.setText (userProfile.getUserName ()  );
                profileEmail.setText ( userProfile.getUserEmail () );



            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }


        } );





    }

}