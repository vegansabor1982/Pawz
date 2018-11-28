package com.example.android.pawstwo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordActivity extends AppCompatActivity {

    private EditText passwordEmail;
    private Button resetpassword;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_password );

        passwordEmail  =(EditText ) findViewById ( R.id.etPasswordEmail );
        resetpassword = (Button) findViewById ( R.id. btnPasswordReset );
        firebaseAuth= FirebaseAuth.getInstance ();

        resetpassword.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                String userEmail=passwordEmail.getText ().toString ().trim ();

                if (userEmail.equals ( " " )){
                    Toast.makeText ( PasswordActivity.this, "Please fill in your email", Toast.LENGTH_LONG ).show ();
                }else{
                    firebaseAuth.sendPasswordResetEmail ( userEmail ).addOnCompleteListener ( new OnCompleteListener<Void> () {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful ()){
                                Toast.makeText ( PasswordActivity.this, "Password reset email sent", Toast.LENGTH_LONG ).show ();
                                finish ();
                                startActivity ( new Intent ( PasswordActivity.this, MainActivity.class ) );

                            }else{
                                Toast.makeText (PasswordActivity.this, "Error: Email not found in the database", Toast.LENGTH_LONG).show ();
                            }

                        }
                    } );
                }


            }
        } );



    }
}
