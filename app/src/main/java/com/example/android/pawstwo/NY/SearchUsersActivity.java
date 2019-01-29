package com.example.android.pawstwo.NY;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pawstwo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUsersActivity extends AppCompatActivity {


    private EditText mSearchField;
    private ImageButton mSearchBtn;

    private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;

    private FirebaseAuth mAuth;

    private SectionsPagerAdapter mSectionsPageAdapter;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_search_users );


        mSearchBtn = findViewById ( R.id.search_btn );
        mSearchField = findViewById ( R.id.search_field);
        mResultList=findViewById ( R.id.result_list );

        mResultList.setHasFixedSize ( true );

        mResultList.setLayoutManager ( new LinearLayoutManager ( this ) );




        mAuth = FirebaseAuth.getInstance ();

        if (mAuth.getCurrentUser () != null) {

            mUserDatabase = FirebaseDatabase.getInstance ().getReference ().child ( "Users" );
        }

      //  mUserDatabase=FirebaseDatabase.getInstance ().getReference ("Users");


        mSearchBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View view ) {

                String searchText = mSearchField.getText ().toString ();

                firebaseUserSearch(searchText);


            }
        } );

    }

    private void firebaseUserSearch(String searchText) {

        mAuth=FirebaseAuth.getInstance ();


        Query query = FirebaseDatabase.getInstance ()
                .getReference ()
                .child ( "Users" );

        Query firebaseSearchQuery= mUserDatabase.orderByChild ( "userName" ).startAt (searchText.trim ().toUpperCase ()  ).endAt ( searchText.trim ().toLowerCase ()+ "\uf8ff" );

        FirebaseRecyclerOptions<NyUsers> options = new FirebaseRecyclerOptions.Builder<NyUsers> ().setQuery ( firebaseSearchQuery, NyUsers.class ).build ();

        FirebaseRecyclerAdapter< NyUsers, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NyUsers, UsersViewHolder> (options  ) {
            @Override
            protected void onBindViewHolder( @NonNull UsersViewHolder viewHolder, int position, @NonNull final NyUsers model ) {


                viewHolder.setDetails (getApplicationContext (), model.getUserName (), model.getImage ());


                final String userId = getRef ( position ).getKey ();


                viewHolder.mView.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick( View view ) {

                        CharSequence options[] = new CharSequence[]{"Open Profile", "Send Message"};

                        AlertDialog.Builder builder = new AlertDialog.Builder ( SearchUsersActivity.this );

                        builder.setTitle ( "Select Option" );

                        builder.setItems ( options, new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick( DialogInterface dialogInterface, int i ) {

                                if (i == 0) {

                                    Intent q = new Intent ( SearchUsersActivity.this, NySpecificUserProfile.class );
                                    q.putExtra ( "userId", userId );
                                    startActivity ( q );

                                }

                                if (i == 1) {

                                    Intent c = new Intent ( SearchUsersActivity.this, PrivateChatActivity.class );
                                    c.putExtra ( "userId", userId );
                                    c.putExtra ( "userName", model.getUserName () );
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

        mResultList.setAdapter ( firebaseRecyclerAdapter );
        firebaseRecyclerAdapter.startListening ();




    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder( @NonNull View itemView ) {
            super ( itemView );

            mView=itemView;
        }

        public void setDetails ( Context ctx ,String userName, String userImage){


            TextView user_name= mView.findViewById ( R.id. ny_user_single_name );

            ImageView user_image = mView.findViewById ( R.id.ny_user_single_image );

            user_name.setText ( userName );

           // CircleImageView userImageView = mView.findViewById ( R.id.ny_user_single_image );

            Picasso.with ( ctx ).load ( userImage ).into ( user_image );



        }


    }
}
