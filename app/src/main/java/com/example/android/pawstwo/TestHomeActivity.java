package com.example.android.pawstwo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.bumptech.glide.Glide;
import com.example.android.pawstwo.NY.FavouritesActivity;
import com.example.android.pawstwo.NY.FoundOnlyActicity;
import com.example.android.pawstwo.NY.LostOnlyActivity;
import com.example.android.pawstwo.NY.SavedChatsActivity;
import com.facebook.FacebookSdk;
import com.facebook.share.Share;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.http.Header;

import static com.example.android.pawstwo.MainActivity.PREFS_NAME;

public class TestHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PetAdapterTest.OnItemClickListener {


    private DrawerLayout mdrawerTest;
    private RecyclerView mRecyclerView;
    private PetAdapterTest mPetAdapterTest;
    private DatabaseReference mDatabaseRef;
    private List<UploadTest> mUploads;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog progressDialogtwo;

    public static final String EXTRA_URL = "imageUrl";
    public static final String PET_TYPE = "pet_type";
    public static final String PET_FAMILY = "pet_family";
    public static final String PET_DESCRIPTION = "pet_description";
    public static final String PET_LATITUDE = "pet_latitude";
    public static final String PET_LONGTITUDE = "pet_longtitude";
    public static final String UPLOADER_NAME = "uploader_name";
    private ImageView mProfileImage;

    private Spinner mSpinner;





    private TextView mName;
    private TextView mEmail;
    //-----------------------------------------------------------------
    private FirebaseUser mCurrentUser;


    private DatabaseReference mUserDatabase;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    private StorageReference mImageStorage;

    private CircleImageView profilePic;
    private TextView profileName;
    private TextView profileEmail;

    //------------------------------------------------






    int number = 0;


    Toolbar mtoolbarTest;



    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_home );



        NavigationView navigationView = findViewById ( R.id.nav_view );

        View header = navigationView.getHeaderView (0 );










        progressDialogtwo = new ProgressDialog ( this );
        firebaseAuth = FirebaseAuth.getInstance ();



        profilePic= header. findViewById ( R.id.smallImage );
        profileName= header.findViewById ( R.id. tvNameHeader );
        profileEmail=header.findViewById ( R.id. tvEmailHeader );


        firebaseAuth=FirebaseAuth.getInstance ();
        firebaseDatabase=FirebaseDatabase.getInstance ();
        firebaseStorage=FirebaseStorage.getInstance ();
        mImageStorage = FirebaseStorage.getInstance().getReference();



        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

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
            mLayoutManager = new LinearLayoutManager ( this );
            (( LinearLayoutManager ) mLayoutManager).setReverseLayout ( true );
            (( LinearLayoutManager ) mLayoutManager).setStackFromEnd ( true );
            mRecyclerView.setLayoutManager ( mLayoutManager );


            mUploads = new ArrayList<> ();


            mDatabaseRef = FirebaseDatabase.getInstance ().getReference ().child ( "Uploads" );

            mDatabaseRef.addValueEventListener ( new ValueEventListener () {
                @Override
                public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                    mUploads.clear ();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren ()) {
                        UploadTest uploadTest = postSnapshot.getValue ( UploadTest.class );

                        mUploads.add ( uploadTest );


                    }


                    mPetAdapterTest = new PetAdapterTest ( TestHomeActivity.this, mUploads );
                    mRecyclerView.setAdapter ( mPetAdapterTest );

                    mPetAdapterTest.setOnItemClickListener ( TestHomeActivity.this );


                    mPetAdapterTest.setOnItemClickListener ( TestHomeActivity.this );


                }

                @Override
                public void onCancelled( @NonNull DatabaseError databaseError ) {

                    Toast.makeText ( TestHomeActivity.this, databaseError.getMessage (), Toast.LENGTH_SHORT ).show ();


                }
            } );



            Intent r = getIntent ();

           /* String firstName = getIntent ().getStringExtra ( "first_name" );
            String lastName = getIntent ().getStringExtra ( "last_name" );
            String email = getIntent ().getStringExtra ( "email" );
            String id = getIntent ().getStringExtra ( "id" );

            profileName.setText ( firstName + " "+  lastName );
            profileEmail.setText ( email );*/


            SharedPreferences settings = getSharedPreferences ( PREFS_NAME, Context.MODE_PRIVATE );

            String firstName = settings.getString ( "first_name", "" );
            String lastName = settings.getString ( "last_name", " " );
            String email = settings.getString ( "email","" );
            profileName.setText ( firstName+ " "+ lastName );
            profileEmail.setText ( email );
























        }
        else{

            mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            String current_uid = mCurrentUser.getUid();

            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
            mUserDatabase.keepSynced(true);

            StorageReference storageReference = firebaseStorage.getReference ();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child( "Users").child (firebaseAuth.getUid ());


            storageReference.child ( firebaseAuth.getUid () ).child ( "Images/Profile Pic" ).getDownloadUrl ().addOnSuccessListener ( new OnSuccessListener<Uri> () {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with (getApplicationContext ()).load ( uri ).fit().centerCrop().into (profilePic  );




                }
            } );

            databaseReference.addValueEventListener ( new ValueEventListener () {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserProfile userProfile =dataSnapshot.getValue (UserProfile.class);
                    profileName.setText (userProfile.getUserName ()  );
                    profileEmail.setText ( userProfile.getUserEmail () );



                }

                @Override
                public void onCancelled( @NonNull DatabaseError databaseError ) {

                }


            } );


        /*/------------------------------------------SPINNER----------------------------------
        mSpinner = findViewById ( R.id.spinner_selection );
        final ArrayList<String> categories = new ArrayList<> ();
        categories.add ( 0, "All" );
        categories.add ( 1, "Lost" );
        categories.add ( 2, "Found" );
        categories.add ( 3, "Gift" );
        categories.add ( 4, "Adopt" );
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter ( this, android.R.layout.simple_spinner_item, categories );
        dataAdapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        mSpinner.setAdapter ( dataAdapter );
        mRecyclerView = findViewById ( R.id.list );
        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setAdapter ( new RecyclerView.Adapter () {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder( @NonNull ViewGroup viewGroup, int i ) {
                return null;
            }
            @Override
            public void onBindViewHolder( @NonNull RecyclerView.ViewHolder viewHolder, int i ) {
            }
            @Override
            public int getItemCount() {
                return 0;
            }
        } );
        mSpinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected( AdapterView<?> adapterView, View view, int position, long itemID ) {
                if (position>=0 && position<categories.size ()){
                    getSelectedCategoryData(position);
                }else{
                    Toast.makeText ( TestHomeActivity.this, "Category doesn't exist", Toast.LENGTH_SHORT ).show ();
                }
            }
            @Override
            public void onNothingSelected( AdapterView<?> adapterView ) {
            }
        } );
        //mRecyclerView.setLayoutManager ( new LinearLayoutManager ( this ) );
        mLayoutManager = new LinearLayoutManager ( this );
        (( LinearLayoutManager ) mLayoutManager).setReverseLayout ( true );
        (( LinearLayoutManager ) mLayoutManager).setStackFromEnd ( true );
        mRecyclerView.setLayoutManager ( mLayoutManager );
//-------------------------------------------------------------------------------------------------*/



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
            mLayoutManager = new LinearLayoutManager ( this );
            (( LinearLayoutManager ) mLayoutManager).setReverseLayout ( true );
            (( LinearLayoutManager ) mLayoutManager).setStackFromEnd ( true );
            mRecyclerView.setLayoutManager ( mLayoutManager );


            mUploads = new ArrayList<> ();


            mDatabaseRef = FirebaseDatabase.getInstance ().getReference ().child ( "Uploads" );

            mDatabaseRef.addValueEventListener ( new ValueEventListener () {
                @Override
                public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                    mUploads.clear ();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren ()) {
                        UploadTest uploadTest = postSnapshot.getValue ( UploadTest.class );

                        mUploads.add ( uploadTest );


                    }


                    mPetAdapterTest = new PetAdapterTest ( TestHomeActivity.this, mUploads );
                    mRecyclerView.setAdapter ( mPetAdapterTest );

                    mPetAdapterTest.setOnItemClickListener ( TestHomeActivity.this );


                    mPetAdapterTest.setOnItemClickListener ( TestHomeActivity.this );


                }

                @Override
                public void onCancelled( @NonNull DatabaseError databaseError ) {

                    Toast.makeText ( TestHomeActivity.this, databaseError.getMessage (), Toast.LENGTH_SHORT ).show ();


                }
            } );



        }








    }

    private void setContentView( int activity_home, int nav_header ) {
    }






    @Override
    public void onItemClick( int position ) {


        Intent d = new Intent ( this, SpecificPetProfileActivity.class );

        UploadTest clickedItem = mUploads.get ( position );

        d.putExtra ( EXTRA_URL, clickedItem.getmImageUrl () );
        d.putExtra ( PET_TYPE, clickedItem.getmType () );
        d.putExtra ( PET_FAMILY, clickedItem.getmFamily () );
        d.putExtra ( PET_DESCRIPTION, clickedItem.getmDescription () );
        d.putExtra ( PET_LATITUDE, clickedItem.getmLat () );
        d.putExtra ( PET_LONGTITUDE, clickedItem.getmLongt () );
        d.putExtra ( UPLOADER_NAME, clickedItem.getmUploaderName () );






        startActivity ( d );

    }

    @Override
    public void onBackPressed() {
        if (mdrawerTest.isDrawerOpen ( GravityCompat.START  )) {

            mdrawerTest.closeDrawer ( GravityCompat.START );
        } else {
            super.onBackPressed ();
        }




    }




    @Override
    public boolean onNavigationItemSelected( @NonNull MenuItem menuItem ) {
        switch (menuItem.getItemId ()) {

            case R.id.nav_profile:

                startActivity ( new Intent ( TestHomeActivity.this, ProfileActivity.class ) );
                mdrawerTest.closeDrawers (  );
                break;

            case R.id.nav_new_entry:

                startActivity ( new Intent ( TestHomeActivity.this, TestUploadActivity.class ) );
                mdrawerTest.closeDrawers (  );
                break;

            case R.id.nav_saved_chats:

                startActivity ( new Intent ( TestHomeActivity.this, SavedChatsActivity.class ) );
                mdrawerTest.closeDrawers (  );
                break;


            case R.id.nav_lost_only:

                startActivity ( new Intent ( TestHomeActivity.this, LostOnlyActivity.class ) );
                mdrawerTest.closeDrawers (  );
                break;

            case R.id.nav_found_only:
                startActivity ( new Intent ( TestHomeActivity.this, FoundOnlyActicity.class ) );
                mdrawerTest.closeDrawers (  );

                break;

            case R.id. nav_favourites:
                startActivity ( new Intent ( TestHomeActivity.this, FavouritesActivity.class ) );
                mdrawerTest.closeDrawers ();

                break;



        }
        return true;



    }

    private void Logout() {
        progressDialogtwo.setMessage ( "Signing out..." );
        progressDialogtwo.show ();
        firebaseAuth = FirebaseAuth.getInstance ();
        firebaseAuth.signOut ();
        finish ();
        Intent i = new Intent ( TestHomeActivity.this, MainActivity.class );
        startActivity ( i );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater ().inflate ( R.menu.menu, menu );


        return true;


    }


    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {


        switch (item.getItemId ()) {
            case R.id.logoutMenu: {
                Logout ();
            }


            break;
        }

        return super.onOptionsItemSelected ( item );
    }









}