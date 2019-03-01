package com.example.android.pawstwo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.FaceDetector;
import android.net.Uri;
import android.se.omapi.Session;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;


import com.bumptech.glide.request.Request;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Share;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText userName;
    private EditText passWord;
    private Button signIn;
    private Button signUp;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Button fbLogin;
    private TextView forgotPassword;
    private CallbackManager mCallbackManager;
    private FacebookAuthCredential authCredential;
    private DatabaseReference mUserDatabase;
    private StorageReference mStorRef;
    private DatabaseReference mDataRef;

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String USERID="userID";



    private static final String TAG="FACELOG";

    public MainActivity() throws Exception {
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate ( savedInstanceState );
        FacebookSdk.setApplicationId ( "1187489271403397" );

        // FacebookSdk.sdkInitialize ( getApplicationContext () );


        //    AppEventsLogger.activateApp(this);
        setContentView ( R.layout.activity_main );




        //----------------FB login------------------------------------
        mCallbackManager = CallbackManager.Factory.create ();
        LoginButton loginButton = findViewById ( R.id.fb_login_button );

        loginButton.setReadPermissions ( "email", "public_profile" );
        loginButton.registerCallback ( mCallbackManager, new FacebookCallback<LoginResult> () {
            @Override
            public void onSuccess( final LoginResult loginResult ) {

                final String userId= loginResult.getAccessToken ().getUserId ();

                GraphRequest graphRequest  = GraphRequest.newMeRequest ( loginResult.getAccessToken (), new GraphRequest.GraphJSONObjectCallback () {
                    @Override
                    public void onCompleted( JSONObject object, GraphResponse response ) {





                        displayUserInfo(object);

                       /* Intent o = new Intent ( MainActivity.this, TestHomeActivity.class );
                        startActivity ( o );
                        finish ();*/





                    }
                });
//---------------------------------------------------------------Add facebook image here-------------------------------------------------------
                Bundle parameters = new Bundle (  );
                parameters.putString ( "fields", "first_name, last_name, email, id" );





                graphRequest.setParameters (parameters  );
                graphRequest.executeAsync ();







            }


            @Override
            public void onCancel() {
                Log.d ( TAG,"facebook:onCancel"  );

            }

            @Override
            public void onError( FacebookException error ) {

                Log.d ( TAG,"facebook:onError"+ error  );

            }
        } );




        //-------------------------------------------------


        mUserDatabase = FirebaseDatabase.getInstance ().getReference ().child ( "Users" );

        userName = findViewById ( R.id.et_Username );
        passWord = findViewById ( R.id.et_Password );
        signIn = findViewById ( R.id.btn_SignIn );
        forgotPassword = ( TextView ) findViewById ( R.id.tvForgotPassword );
        // fbLogin=findViewById ( R.id.fb_login_button );

        // signUp =findViewById ( R.id.btn_SignUp );

        firebaseAuth = FirebaseAuth.getInstance ();
        progressDialog = new ProgressDialog ( this );
      /*  fbLogin.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent k = new Intent ( MainActivity.this, HomeActivity.class );
                startActivity(k);
            }
        } );*/

        FirebaseUser user = firebaseAuth.getCurrentUser ();


        if (user != null) {
            finish ();
            startActivity ( new Intent ( MainActivity.this, TestHomeActivity.class ) );
        }


        Button signIn = ( Button ) findViewById ( R.id.btn_SignIn );

        signIn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View view ) {

                if (userName.getText ().toString ().length () == 0 || passWord.getText ().toString ().length () == 0) {

                    Toast.makeText ( MainActivity.this, "Details Missing", Toast.LENGTH_SHORT ).show ();
                } else {
                    validate ( userName.getText ().toString (), passWord.getText ().toString () );
                    // startActivity ( new Intent ( MainActivity.this, HomeActivity.class ) );

                }

            }

        } );


        Button signUp = ( Button ) findViewById ( R.id.btn_SignUp );

        signUp.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View view ) {
                startActivity ( new Intent ( MainActivity.this, SignUpActivity.class ) );
            }
        } );

        forgotPassword.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View view ) {
                startActivity ( new Intent ( MainActivity.this, PasswordActivity.class ) );
            }
        } );


    }





    @Override
    public void onStart() {


        super.onStart ();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser ();

        if (currentUser!=null){
            updateUI();
        }

    }

    private void updateUI() {

        Toast.makeText ( MainActivity.this, "You are logged in", Toast.LENGTH_SHORT ).show ();

        Intent o = new Intent ( MainActivity.this, TestHomeActivity.class );
        startActivity ( o );
        finish ();
    }

    private void handleFacebookAccessToken( AccessToken token ){

        Log.d ( TAG, "handleFaecbookAccessToken:" +token);

        AuthCredential credential = FacebookAuthProvider.getCredential ( token.getToken () );
        firebaseAuth.signInWithCredential ( credential).addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete( @NonNull Task<AuthResult> task ) {

                if (task.isSuccessful ()){

                    Log.d ( TAG, "signInWithCredential: success" );
                    FirebaseUser user =firebaseAuth.getCurrentUser ();
                    updateUI ();


                }else{

                    Log.d ( TAG, "signInWithCredential: failure", task.getException () );
                    Toast.makeText ( MainActivity.this,"Authentication Failed", Toast.LENGTH_SHORT  ).show ();

                    updateUI ();
                }

            }
        } );


    }


    private void validate( String userName, String userPassword ) {

        progressDialog.setMessage ( "Signing in..." );
        progressDialog.show ();

        firebaseAuth.signInWithEmailAndPassword ( userName, userPassword ).addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete( @NonNull Task<AuthResult> task ) {
                if (task.isSuccessful ()) {
                    progressDialog.dismiss ();

                    String current_user_id = firebaseAuth.getCurrentUser ().getUid ();

                    String deviceToken = FirebaseInstanceId.getInstance ().getToken ();

                    mUserDatabase.child ( current_user_id ).child ( "device token" ).setValue ( deviceToken ).addOnSuccessListener ( new OnSuccessListener<Void> () {
                        @Override
                        public void onSuccess( Void aVoid ) {
                            //Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            checkEmailVerification ();


                        }
                    } );


                } else {
                    progressDialog.dismiss ();
                    Toast.makeText ( MainActivity.this, "Login Failed", Toast.LENGTH_SHORT ).show ();

                }
            }


        } );


    }


    private void checkEmailVerification() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance ().getCurrentUser ();
        Boolean emailflag = firebaseUser.isEmailVerified ();

        if (emailflag) {
            finish ();
            startActivity ( new Intent ( MainActivity.this, TestHomeActivity.class ) );
        } else {
            Toast.makeText ( this, "Verify your Email", Toast.LENGTH_LONG ).show ();
            firebaseAuth.signOut ();
        }


    }

    private void displayUserInfo( JSONObject object ) {

        String first_name,  last_name , email, id ;

        first_name="";
        last_name="";
        email="";
        id="";



        try {
            first_name=object.getString ( "first_name" );
            last_name=object.getString ( "last_name" );
            email=object.getString ( "email" );
            id=object.getString ( "id" );





        } catch (JSONException e) {
            e.printStackTrace ();
        }

        /*Intent d = new Intent ( this, TestHomeActivity.class );

        d.putExtra ( "first_name", first_name);
        d.putExtra ( "last_name", last_name );
        d.putExtra (  "email", email );
        d.putExtra ( id, "id" );

        startActivity ( d );*/


        SharedPreferences settings = getSharedPreferences ( PREFS_NAME, Context.MODE_PRIVATE );

        SharedPreferences.Editor editor = settings.edit ();

        editor.putString ( "first_name", first_name );
        editor.putString ( "last_name", last_name );
        editor.putString ( "email", email );


        editor.commit ();

        Intent d = new Intent ( this, TestHomeActivity.class );
        startActivity ( d );


























    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data ) {
        super.onActivityResult ( requestCode, resultCode, data );

        mCallbackManager.onActivityResult ( requestCode, resultCode, data );
    }



























}

/*if (user==null){
        Toast.makeText(MainActivity.this, "Fields are blank", Toast.LENGTH_SHORT).show();
        }*/