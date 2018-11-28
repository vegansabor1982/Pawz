package com.example.android.pawstwo;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private Button button;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialogtwo;
    private ClipData.Item openProfile;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_home );
        progressDialogtwo = new ProgressDialog ( this );


        android.support.v7.widget.Toolbar toolbar = findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );


        drawer = findViewById ( R.id.drawer_layout );

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle ( this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener ( toggle );
        toggle.syncState ();



    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen ( GravityCompat.START )) {

            drawer.closeDrawer ( GravityCompat.START );
        } else {
            super.onBackPressed ();
        }


    }

    private void Logout() {
        progressDialogtwo.setMessage ( "Signing out..." );
        progressDialogtwo.show ();
        firebaseAuth= FirebaseAuth.getInstance ();
        firebaseAuth.signOut ();
        finish ();
       Intent i = new Intent ( HomeActivity.this, MainActivity.class  );
       startActivity ( i );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId()){
            case R.id.logoutMenu:{
                Logout ();
            }


           break;
        }

        return super.onOptionsItemSelected(item);
    }









}