package com.example.android.pawstwo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TestHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,PetAdapterTest.OnItemClickListener {


    private DrawerLayout mdrawerTest;
    private RecyclerView mRecyclerView;
    private PetAdapterTest mPetAdapterTest;
    private DatabaseReference mDatabaseRef;
    private List <UploadTest> mUploads;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog progressDialogtwo;
    private FirebaseAuth firebaseAuth;
    public static final String EXTRA_URL ="imageUrl";
    public static final String PET_TYPE ="pet_type";
    public static final String PET_FAMILY ="pet_family";
    public static final String PET_DESCRIPTION ="pet_description";
    public static final String PET_LATITUDE ="pet_latitude";
    public static final String PET_LONGTITUDE ="pet_longtitude";
    public static final String UPLOADER_NAME ="uploader_name";


    int number = 0;


    Toolbar mtoolbarTest;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_home );
        progressDialogtwo = new ProgressDialog ( this );
        firebaseAuth=FirebaseAuth.getInstance ();







        NavigationView navigationView = findViewById ( R.id.nav_view );

        mdrawerTest = findViewById ( R.id.drawer_layout );

        navigationView.setNavigationItemSelectedListener ( this );



        android.support.v7.widget.Toolbar toolbar = findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle ( this, mdrawerTest, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        mdrawerTest.addDrawerListener ( toggle );
        toggle.syncState ();

        mRecyclerView = findViewById ( R.id.list );
        mRecyclerView.setHasFixedSize ( true );

        //mRecyclerView.setLayoutManager ( new LinearLayoutManager ( this ) );
        mLayoutManager=new LinearLayoutManager ( this );
        (( LinearLayoutManager ) mLayoutManager).setReverseLayout ( true );
        (( LinearLayoutManager ) mLayoutManager).setStackFromEnd ( true );
        mRecyclerView.setLayoutManager ( mLayoutManager );



        mUploads = new ArrayList<> (  );



        mDatabaseRef=FirebaseDatabase.getInstance ().getReference().child("Uploads");

        mDatabaseRef.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploads.clear ();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren ()){
                    UploadTest uploadTest = postSnapshot.getValue (UploadTest.class);

                    mUploads.add ( uploadTest );


                }


                mPetAdapterTest = new PetAdapterTest (TestHomeActivity.this,mUploads  );
                mRecyclerView.setAdapter ( mPetAdapterTest );

                mPetAdapterTest.setOnItemClickListener ( TestHomeActivity.this );


                mPetAdapterTest.setOnItemClickListener ( TestHomeActivity.this );


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText ( TestHomeActivity.this, databaseError.getMessage (),Toast.LENGTH_SHORT ).show ();


            }
        } );



    }


    @Override
    public void onItemClick(int position) {


       Intent d = new Intent ( this, SpecificPetProfileActivity.class );

       UploadTest clickedItem = mUploads.get ( position );

       d.putExtra ( EXTRA_URL, clickedItem.getmImageUrl () );
       d.putExtra ( PET_TYPE,clickedItem.getmType ());
       d.putExtra ( PET_FAMILY,clickedItem.getmFamily () );
       d.putExtra ( PET_DESCRIPTION,clickedItem.getmDescription () );
       d.putExtra ( PET_LATITUDE,clickedItem.getmLat () );
       d.putExtra ( PET_LONGTITUDE,clickedItem.getmLongt () );
       d.putExtra ( UPLOADER_NAME,clickedItem.getmUploaderName () );


       startActivity ( d );

    }
    @Override
    public void onBackPressed() {
        if (mdrawerTest.isDrawerOpen ( GravityCompat.START )) {

            mdrawerTest.closeDrawer ( GravityCompat.START );
        } else {
            super.onBackPressed ();
        }


    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId ()){
            case R.id.nav_profile:

                startActivity ( new Intent ( TestHomeActivity.this, ProfileActivity.class ) );
                break;

            case R.id.nav_new_entry:

                startActivity ( new Intent ( TestHomeActivity.this, TestUploadActivity.class ) );
                break;
        }
        return true;
    }

    private void Logout() {
        progressDialogtwo.setMessage ( "Signing out..." );
        progressDialogtwo.show ();
        firebaseAuth= FirebaseAuth.getInstance ();
        firebaseAuth.signOut ();
        finish ();
        Intent i = new Intent ( TestHomeActivity.this, MainActivity.class  );
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




}
