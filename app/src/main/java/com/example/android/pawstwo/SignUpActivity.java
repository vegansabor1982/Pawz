package com.example.android.pawstwo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {


    private EditText userName, userPassword, userEmail;
    private Button regButton;
    private FirebaseAuth firebaseAuth;
    private ImageView userProfilePic;
    String name,email, password,imageurl;
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE=123;
    Uri imagePath;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PICK_IMAGE && resultCode==RESULT_OK && data.getData ()!=null){

            imagePath=data.getData ();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver () ,imagePath );
                userProfilePic.setImageBitmap ( bitmap );
            } catch (IOException e) {
                e.printStackTrace ();
            }

        }
        super.onActivityResult ( requestCode, resultCode, data );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_sign_up );




        setupUIViews ();

        firebaseAuth = FirebaseAuth.getInstance ();
        firebaseStorage= FirebaseStorage.getInstance ();

        storageReference= firebaseStorage.getReference ();
        userProfilePic.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent glry = new Intent();
                glry.setType ( "image/*" );
                glry.setAction (Intent.ACTION_GET_CONTENT  );
                startActivityForResult ( Intent.createChooser ( glry, "Select Image" ), PICK_IMAGE );

            }
        } );






        regButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                if (validate ()) {

                    // Upload data to database
                    String user_email = userEmail.getText ().toString ().trim ();
                    String user_password = userPassword.getText ().toString ().trim ();




                    firebaseAuth.createUserWithEmailAndPassword ( user_email, user_password ).addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {





                            if (task.isSuccessful ()) {
                                sendEmailVerification ();
                            } else {
                                Toast.makeText ( SignUpActivity.this, "Registration Failed", Toast.LENGTH_SHORT ).show ();

                            }


                        }
                    } );


                }


            }


        } );

    }











    private void setupUIViews() {

        userName = ( EditText ) findViewById ( R.id.Name );
        userPassword = ( EditText ) findViewById ( R.id.Password );
        userEmail = ( EditText ) findViewById ( R.id.Email );
        regButton = ( Button ) findViewById ( R.id.Register );
        userProfilePic= (ImageView) findViewById ( R.id.ivProfile );


    }


    private Boolean validate() {

        Boolean result = false;

        name = userName.getText ().toString ();
        password = userPassword.getText ().toString ();
        email = userEmail.getText ().toString ();

        if (name.isEmpty () || password.isEmpty () || email.isEmpty ()|| imagePath ==null) {

            Toast.makeText ( this, "Details Missing", Toast.LENGTH_SHORT ).show ();
        } else {
            result = true;
        }
        return result;

    }

    private void sendEmailVerification(){

        final FirebaseUser firebaseUser =firebaseAuth.getCurrentUser ();
        if (firebaseUser!=null){
            firebaseUser.sendEmailVerification ().addOnCompleteListener ( new OnCompleteListener<Void> () {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful ()){
                        sendUserData ();
                        Toast.makeText ( SignUpActivity.this, "Verification mail has been sent", Toast.LENGTH_LONG ).show ();
                        firebaseAuth.signOut ();
                        finish ();
                        startActivity ( new Intent ( SignUpActivity.this, MainActivity.class ) );
                    }else{
                        Toast.makeText ( SignUpActivity.this,"Verification mail hasn't been sent", Toast.LENGTH_LONG ).show ();
                    }

                }
            } );

        }
    }

    private void sendUserData(){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance ();
        final DatabaseReference myRef = firebaseDatabase.getReference().child ("Users").child ( firebaseAuth.getUid () );





        StorageReference imageReference = storageReference.child ( firebaseAuth.getUid () ).child ( "Images").child ( "Profile Pic" );
        UploadTask uploadTask = imageReference.putFile ( imagePath );
        uploadTask.addOnFailureListener ( new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText ( SignUpActivity.this, "Upload failed", Toast.LENGTH_LONG ).show ();

            }
        } );
        uploadTask.addOnSuccessListener ( new OnSuccessListener<UploadTask.TaskSnapshot> () {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();

                Toast.makeText ( SignUpActivity.this, "Upload Successful", Toast.LENGTH_LONG ).show ();

                UserProfile userProfile = new UserProfile ( name, email,downloadUrl.toString () );
                myRef.setValue (userProfile  );

            }
        } );


    }



}

