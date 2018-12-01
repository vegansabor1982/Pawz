package com.example.android.pawstwo;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Button button;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private ProgressDialog progressDialogtwo;
    private ClipData.Item openProfile;
    private Menu drawer_menu;
    Toolbar toolbar;
    private ImageView smallProfilePic;
    private TextView profileEmail;
    private TextView profileName;
    private ListView listView;
   // private MenuItem menuItem2;
    //private MenuItem menuItem;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_home );
        progressDialogtwo = new ProgressDialog ( this );
        NavigationView navigationView = findViewById ( R.id.nav_view );
        drawer = findViewById ( R.id.drawer_layout );
        navigationView.setNavigationItemSelectedListener ( this );
       // menuItem2=findViewById ( R.id.nav_new_entry );
       // menuItem=findViewById ( R.id.nav_profile );





















        android.support.v7.widget.Toolbar toolbar = findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );


        drawer = findViewById ( R.id.drawer_layout );







        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle ( this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener ( toggle );
        toggle.syncState ();















    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId ()){
            case R.id.nav_profile:

                startActivity ( new Intent ( HomeActivity.this, ProfileActivity.class ) );
                break;

            case R.id.nav_new_entry:

                startActivity ( new Intent ( HomeActivity.this, NewEntryActivity.class ) );
                break;
        }
        return true;
    }


    /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId ()){
            case R.id.nav_profile:
                getSupportFragmentManager ().beginTransaction ().replace ( R.id.fragment_container, new ProfileFragment ()).commit ();
                break;
        }
        drawer.closeDrawer ( GravityCompat.START );
        return true;
    }*/

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


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_profile, container, false );


    }

   /*public Boolean  onMenuPressed(Menu menu){
        firebaseStorage2=FirebaseStorage.getInstance ();
        StorageReference storageReference2 = firebaseStorage2.getReference ();

        smallProfilePic=(ImageView ) findViewById ( R.id.smallImage ) ;


        storageReference2.child(firebaseAuth.getUid ()).child ( "Images/Profile Pic" ).getDownloadUrl ().addOnSuccessListener ( new OnSuccessListener<Uri> () {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get ().load ( uri ).fit().centerCrop().into (smallProfilePic  );

            }

        } ); return true;


    }*/













}