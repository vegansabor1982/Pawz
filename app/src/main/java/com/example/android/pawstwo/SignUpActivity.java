package com.example.android.pawstwo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.RingtonePreference;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class SignUpActivity extends AppCompatActivity {


    private EditText userName, userPassword, userEmail;
    private Button regButton;
    private FirebaseAuth firebaseAuth;
    private ImageView userProfilePic;
    String email, name, password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_sign_up );




        setupUIViews ();

        firebaseAuth = FirebaseAuth.getInstance ();

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

        if (name.isEmpty () || password.isEmpty () || email.isEmpty ()) {

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
        DatabaseReference myRef = firebaseDatabase.getReference (firebaseAuth.getUid ());
        UserProfile userProfile = new UserProfile ( name, email );
        myRef.setValue (userProfile  );
    }



}




