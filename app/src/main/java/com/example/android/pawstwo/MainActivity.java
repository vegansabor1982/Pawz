package com.example.android.pawstwo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private EditText userName;
    private EditText passWord;
    private Button signIn;
    private Button signUp;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
  //  private Button fbLogin;
   // private CallbackManager fbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        userName = findViewById ( R.id.et_Username );
        passWord = findViewById ( R.id.et_Password );
        signIn = findViewById ( R.id.btn_SignIn );
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
           startActivity ( new Intent ( MainActivity.this, HomeActivity.class ) );
        }
        

        Button signIn = ( Button ) findViewById ( R.id.btn_SignIn );

        signIn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                validate ( userName.getText ().toString (), passWord.getText ().toString () );
               // startActivity ( new Intent ( MainActivity.this, HomeActivity.class ) );

            }
        } );




        Button signUp = ( Button ) findViewById ( R.id.btn_SignUp );

        signUp.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                startActivity ( new Intent ( MainActivity.this, SignUpActivity.class ) );
            }
        } );


    }

    private void validate(String userName, String userPassword) {

        progressDialog.setMessage ( "Signing in..." );
        progressDialog.show ();

        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                   Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity ( new Intent ( MainActivity.this, HomeActivity.class ) );
                }else {
                    progressDialog.dismiss ();
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                    }
                }




        });





    }





}

/*if (user==null){

        Toast.makeText(MainActivity.this, "Fields are blank", Toast.LENGTH_SHORT).show();
        }*/
