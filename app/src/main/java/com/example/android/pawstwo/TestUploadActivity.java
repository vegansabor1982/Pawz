package com.example.android.pawstwo;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.DoubleNode;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TestUploadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Spinner spinner1;
    private Spinner spinner2;
    private EditText mDescription;
    private ImageView mPetPic;
    private Button mUpload;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private Button mMaps;
    private TextView mLat;
    private TextView mLongt;
    private TextView profileName;



    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;



    String Type_Test;
    String Family_Test;
    String Description_Test;
    String userNameTwo;
    String emailTwo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_test_upload );



        mLat= findViewById ( R.id.tv_latitude );
        mLongt=findViewById ( R.id.tv_longtitude );


        Bundle bundle = getIntent ().getExtras ();
        double coords= bundle.getDouble ( "LATITUDE" );
        double coords2=bundle.getDouble ( "LONGTITUDE" );

        mLat.setText ( "Latitude :" +String.valueOf ( coords )  );
        mLongt.setText ( "Longtitude :"+String.valueOf ( coords2 ) );



        mFirebaseAuth=FirebaseAuth.getInstance ();
        mFirebaseDatabase=FirebaseDatabase.getInstance ();





























                spinner1 = findViewById ( R.id.spinner_type_test );
        spinner2 = findViewById ( R.id.spinner_family_test );
        mDescription = findViewById ( R.id.tv_description_test );
        mPetPic = findViewById ( R.id.iv_petpic_test );
        mUpload = findViewById ( R.id.btn_upload_test );

        mMaps=findViewById ( R.id.btn_maps );

        mFirebaseAuth=FirebaseAuth.getInstance ();



        mStorageRef = FirebaseStorage.getInstance ().getReference ( "Uploads" );
        mDatabaseRef = FirebaseDatabase.getInstance ().getReference ( ).child("Uploads" );


        mUpload.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                uploadFile ();
                startActivity ( new Intent ( TestUploadActivity.this, TestHomeActivity.class ) );


            }
        } );

        mPetPic.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                openFileChooser ();

            }
        } );
        mMaps.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                startActivity ( new Intent ( TestUploadActivity.this, MapsActivity.class ) );
            }
        } );




//----------------------------------------------------------------------------------------------------------------------------------------------

        ArrayList<String> categories = new ArrayList<> ();
        categories.add ( 0, "Select Category" );
        categories.add ( 1, "Found" );
        categories.add ( 2, "Lost" );
        categories.add ( 3, "Gift" );
        categories.add ( 4, "Adopt" );


        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter ( this, android.R.layout.simple_spinner_item, categories );
        dataAdapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );

        spinner1.setAdapter ( dataAdapter );

        ArrayList<String> animals = new ArrayList<> ();

        animals.add ( 0, "Select Family" );
        animals.add ( 1, "Dogs" );
        animals.add ( 2, "Cats" );
        animals.add ( 3, "Other" );


        ArrayAdapter<String> dataAdapter2;
        dataAdapter2 = new ArrayAdapter ( this, android.R.layout.simple_spinner_item, animals );
        dataAdapter2.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );

        spinner2.setAdapter ( dataAdapter2 );
//   ----------------------------------------------------------------------------------------------------------------------------------------------

    }

    @Override
    public void onBackPressed() {

        Intent u =new Intent ( this, TestHomeActivity.class );
        startActivity ( u );

    }

    private void openFileChooser() {

        Intent intent = new Intent ();
        intent.setType ( "image/*" );
        intent.setAction ( Intent.ACTION_GET_CONTENT );
        startActivityForResult ( intent, PICK_IMAGE_REQUEST );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData () != null) {
            mImageUri = data.getData ();

            Picasso.get ().load ( mImageUri ).fit ().centerCrop ().into ( mPetPic );
        }
    }

   /* private Boolean validate() {
        Boolean result = false;
        Type_Test = spinner1.getSelectedItem ().toString ();
        Family_Test = spinner2.getSelectedItem ().toString ();
        Description_Test = mDescription.getText ().toString ();
        if (Type_Test.isEmpty () || Family_Test.isEmpty () || mDescription == null) {
            Toast.makeText ( this, "Details Missing", Toast.LENGTH_SHORT ).show ();
        } else {
            result = true;
        }
        return result;
    }*/

    private String getFileExtension(Uri uri) {


        ContentResolver cR = getContentResolver ();
        MimeTypeMap mime = MimeTypeMap.getSingleton ();
        return mime.getExtensionFromMimeType ( cR.getType (uri) );
    }


    private void uploadFile(){

        if (mImageUri!= null){

            StorageReference fileReference = mStorageRef.child ( System.currentTimeMillis ()+"."+getFileExtension ( mImageUri ) );


            fileReference.putFile ( mImageUri ).addOnSuccessListener ( new OnSuccessListener<UploadTask.TaskSnapshot> () {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   /* Handler handler = new Handler ();
                    handler.postDelayed ( new Runnable () {
                        @Override
                        public void run() {
                            mProgressBar.setProgress ( 0 );
                        }
                    } ,500);*/

                    Toast.makeText ( TestUploadActivity.this, "Upload Successfull",Toast.LENGTH_SHORT).show ();


                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();
                    // Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString());

                    mFirebaseAuth=FirebaseAuth.getInstance ();

                    DatabaseReference myRefTwo = mDatabaseRef.getRef ().child ( "Users" ).child ( mFirebaseAuth.getUid () );


                    UploadTest uploadTest = new UploadTest ( spinner1.getSelectedItem ().toString ().trim (),spinner2.getSelectedItem ().toString ().trim (),mDescription.getText ().toString ().trim (),downloadUrl.toString (),mLat.getText ().toString (), mLongt.getText ().toString (),mFirebaseAuth.getUid ());

                    String uploadId = mDatabaseRef.push ().getKey ();
                    mDatabaseRef.child(uploadId).setValue ( uploadTest );






                }

            } )
                    .addOnFailureListener ( new OnFailureListener () {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText ( TestUploadActivity.this, e.getMessage (), Toast.LENGTH_SHORT ).show ();

                        }
                    } );



        }else{
            Toast.makeText ( this, "Details Missing", Toast.LENGTH_SHORT ).show ();
        }


    }
}