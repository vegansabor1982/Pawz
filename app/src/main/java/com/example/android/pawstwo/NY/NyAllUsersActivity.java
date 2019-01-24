package com.example.android.pawstwo.NY;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.android.pawstwo.ProfileActivity;
import com.example.android.pawstwo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class NyAllUsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUsersList;
    private DatabaseReference mUsersDatabase;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_ny_all_users );

        mToolbar = findViewById ( R.id.ny_all_users_appBar );
        setSupportActionBar ( mToolbar );
        getSupportActionBar ().setTitle ( "All Users" );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

        mAuth = FirebaseAuth.getInstance ();

        if (mAuth.getCurrentUser () != null) {

            mUsersDatabase = FirebaseDatabase.getInstance ().getReference ().child ( "Users" ).child ( mAuth.getCurrentUser ().getUid () );
        }


        mUsersList = findViewById ( R.id.ny_users_list );
        mUsersList.setHasFixedSize ( true );

        mUsersList.setLayoutManager ( new LinearLayoutManager ( this ) );


    }


    @Override
    protected void onStart() {
        super.onStart ();

        mAuth = FirebaseAuth.getInstance ();


        Query query = FirebaseDatabase.getInstance ()
                .getReference ()
                .child ( "Users" );


        FirebaseRecyclerOptions<NyUsers> options = new FirebaseRecyclerOptions.Builder<NyUsers> ().setQuery ( query, NyUsers.class ).build ();
        FirebaseRecyclerAdapter<NyUsers, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NyUsers, UsersViewHolder> ( options ) {


            @Override
            protected void onBindViewHolder( @NonNull UsersViewHolder usersViewHolder, int position, @NonNull final NyUsers users ) {

                usersViewHolder.setName ( users.getUserName () );
                usersViewHolder.setUserImage ( users.getImage (), getApplicationContext () );

                final String userId = getRef ( position ).getKey ();

              /*  usersViewHolder.mView.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick( View view ) {

                        Intent l = new Intent ( NyAllUsersActivity.this, NySpecificUserProfile.class );

                        l.putExtra ( "userId", userId );

                        startActivity ( l );


                    }
                } );*/

                usersViewHolder.mView.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick( View view ) {

                        CharSequence options[] = new CharSequence[]{"Open Profile", "Send Message"};

                        AlertDialog.Builder builder = new AlertDialog.Builder ( NyAllUsersActivity.this );

                        builder.setTitle ( "Select Option" );

                        builder.setItems ( options, new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick( DialogInterface dialogInterface, int i ) {

                                if (i == 0) {

                                    Intent q = new Intent ( NyAllUsersActivity.this, NySpecificUserProfile.class );
                                    q.putExtra ( "userId", userId );
                                    startActivity ( q );

                                }

                                if (i == 1) {

                                    Intent c = new Intent ( NyAllUsersActivity.this, PrivateChatActivity.class );
                                    c.putExtra ( "userId", userId );
                                    c.putExtra ( "userName", users.getUserName () );
                                    startActivity ( c );


                                }


                            }

                        } );
                        builder.show ();


                    }
                } );

            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder( @NonNull ViewGroup viewGroup, int i ) {

                View view = LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.ny_users_single_layout, viewGroup, false );

                return new UsersViewHolder ( view );


            }


        };

        mUsersList.setAdapter ( firebaseRecyclerAdapter );
        firebaseRecyclerAdapter.startListening ();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder( View itemView ) {

            super ( itemView );
            mView = itemView;

        }

        public void setName( String name ) {
            TextView mUserNameView = mView.findViewById ( R.id.ny_user_single_name );
            mUserNameView.setText ( name );
        }

        public void setUserImage( String image, Context ctx ) {


            CircleImageView userImageView = mView.findViewById ( R.id.ny_user_single_image );

            Picasso.with ( ctx ).load ( image ).placeholder ( R.drawable.com_facebook_profile_picture_blank_portrait ).into ( userImageView );


        }


    }


}
