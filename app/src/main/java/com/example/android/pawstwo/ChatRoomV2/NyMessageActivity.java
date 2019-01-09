package com.example.android.pawstwo.ChatRoomV2;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.android.pawstwo.MainActivity;
import com.example.android.pawstwo.R;
import com.example.android.pawstwo.UserProfile;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NyMessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    Toolbar toolbar;
    FirebaseStorage firebaseStorage;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_ny_message );

        toolbar= findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );
//        getSupportActionBar ().setTitle ( " " );

        profile_image=findViewById ( R.id.ny_profile_image );
        username=findViewById ( R.id.ny_username );

        firebaseUser=FirebaseAuth.getInstance ().getCurrentUser ();
        reference=FirebaseDatabase.getInstance ().getReference ("Users").child ( firebaseUser.getUid () );
        firebaseAuth=FirebaseAuth.getInstance ();

        reference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                UserProfile user = dataSnapshot.getValue (UserProfile.class);
                username.setText ( user.getUserName () );

              /*  if (user.getImageUrl ().equals ( "default" )){
                    profile_image.setImageResource ( R.mipmap.ic_launcher );
                }else{
                    Glide.with ( NyMessageActivity.this ).load ( user.getImageUrl () ).into ( profile_image );
                }*/


                firebaseStorage = FirebaseStorage.getInstance ();
                firebaseDatabase=FirebaseDatabase.getInstance ();
                firebaseStorage=FirebaseStorage.getInstance ();

                StorageReference storageReference = firebaseStorage.getReference ();


                DatabaseReference databaseReference = firebaseDatabase.getReference().child( "Users").child (firebaseAuth.getUid ());


                storageReference.child ( firebaseAuth.getUid () ).child ( "Images/Profile Pic" ).getDownloadUrl ().addOnSuccessListener ( new OnSuccessListener<Uri> () {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get ().load ( uri ).fit().centerCrop().into (profile_image  );




                    }
                } );


            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );

        TabLayout tabLayout = findViewById ( R.id.ny_tab_layout );
        ViewPager viewPager = findViewById ( R.id.ny_view_pager );

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter ( getSupportFragmentManager () );

        viewPagerAdapter.addFragment ( new NyChatsFragment (), "Chats" );
        viewPagerAdapter.addFragment ( new NyUsersFragment (), "Users" );


        viewPager.setAdapter ( viewPagerAdapter );
        tabLayout.setupWithViewPager ( viewPager );


    }
    public boolean onCreateOptionsMenu( Menu menu ){
        getMenuInflater ().inflate ( R.menu.ny_menu,menu );
        return true;


    }

    @Override
    public boolean onOptionsItemSelected ( MenuItem item ){

        switch (item.getItemId ()){
            case R.id.ny_logout:
                FirebaseAuth.getInstance ().signOut ();
            startActivity ( new Intent ( NyMessageActivity.this,MainActivity.class ) );
            finish ();
            return true;
        }
        return false;


    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter( FragmentManager fm ){

            super(fm);
            this.fragments= new ArrayList<> (  );
            this.titles=new ArrayList<> (  );

        }


        @Override
        public Fragment getItem( int position ) {
            return fragments.get ( position );
        }

        @Override
        public int getCount() {
            return fragments.size ();
        }

        public void addFragment (Fragment fragment, String title){

            fragments.add ( fragment );
            titles.add ( title );
        }


        @Nullable
        @Override
        public CharSequence getPageTitle( int position ) {
            return titles.get ( position );
        }
    }
}
