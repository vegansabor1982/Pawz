package com.example.android.pawstwo.NY;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.pawstwo.R;
import com.example.android.pawstwo.TestHomeActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleLogIn extends AppCompatActivity {

    private SignInButton mGoogleBtn;

    private static final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;

    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    private static final String TAG= "MAIN_ACTIVITY" ;

    private FirebaseAuth.AuthStateListener mAuthStateListener;




    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        mAuth=FirebaseAuth.getInstance ();
        mAuthStateListener = new FirebaseAuth.AuthStateListener () {
            @Override
            public void onAuthStateChanged( @NonNull FirebaseAuth firebaseAuth ) {

                if (firebaseAuth.getCurrentUser ()!= null){

                    startActivity ( new Intent ( GoogleLogIn.this, TestHomeActivity.class ) );


                }

            }
        };

        mGoogleBtn = findViewById ( R.id. google_img);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient=new GoogleApiClient.Builder ( getApplicationContext () ).enableAutoManage ( this, new GoogleApiClient.OnConnectionFailedListener () {
            @Override
            public void onConnectionFailed( @NonNull ConnectionResult connectionResult ) {

                Toast.makeText ( GoogleLogIn.this, "Error", Toast.LENGTH_SHORT ).show ();

            }
        } ).addApi ( Auth.GOOGLE_SIGN_IN_API, gso ).build ();

        mGoogleBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View view ) {
                signIn ();
            }
        } );



    }

    @Override
    protected void onStart() {
        super.onStart ();

        mAuth.addAuthStateListener ( mAuthStateListener );
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent ( mGoogleApiClient );
        startActivityForResult ( signInIntent, RC_SIGN_IN );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent ( data );
            if (result.isSuccess ()){

                GoogleSignInAccount account = result.getSignInAccount ();
                firebaseAuthWithGoogle ( account );
            }else{


            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
      //  Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult> () {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                            Log.w(TAG, "signInWithCredential:onComplete" + task.isSuccessful ());
                          if (!task.isSuccessful ()){

                              Log.w(TAG, "signInWithCredential" , task.getException ());
                              Toast.makeText ( GoogleLogIn.this, "Authentication Failed", Toast.LENGTH_SHORT ).show ();
                          }
                    }
                });
    }
}
