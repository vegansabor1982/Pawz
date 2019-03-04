package com.example.android.pawstwo.ChatVersionTwo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.android.pawstwo.NY.Messages;
import com.example.android.pawstwo.NY.NySpecificUserProfile;
import com.example.android.pawstwo.NY.NyUsers;
import com.example.android.pawstwo.NY.PrivateChatActivity;
import com.example.android.pawstwo.NY.SearchUsersActivity;
import com.example.android.pawstwo.R;
import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;

    private List<NyUsers> mUsers;

    private ImageButton mSearch;
    private EditText mSearchText;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View view = inflater.inflate ( R.layout.fragment_users, container, false );

        mSearch=view.findViewById ( R.id.search_btn );
        mSearchText=view.findViewById ( R.id.search_field );
        recyclerView=view.findViewById ( R.id. users_recycler_view );










        recyclerView = view.findViewById ( R.id.users_recycler_view );
        recyclerView.setHasFixedSize ( true );
        recyclerView.setLayoutManager ( new LinearLayoutManager ( getContext () ) );

        mUsers = new ArrayList<> ();

      //  readUsers ();

        mAuth = FirebaseAuth.getInstance ();

        if (mAuth.getCurrentUser () != null) {

            mUserDatabase = FirebaseDatabase.getInstance ().getReference ().child ( "Users" );
        }

        mSearch.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View view ) {

                String searchText = mSearchText.getText ().toString ();

                firebaseUserSearch(searchText);


            }
        } );



        return view;



    }

    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance ().getCurrentUser ();
        DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ( "Users" );

        reference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                mUsers.clear ();
                for (DataSnapshot snapshot : dataSnapshot.getChildren ()) {
                    NyUsers nyUsers = snapshot.getValue ( NyUsers.class );

                    //assert userModel != null;
                    //assert firebaseUser != null;


                    mUsers.add ( nyUsers );


                    userAdapter = new UserAdapter ( getContext (), mUsers );
                    recyclerView.setAdapter ( userAdapter );

                }




            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

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


                viewHolder.setDetails (getContext (), model.getUserName (), model.getImage ());


                final String userId = getRef ( position ).getKey ();


                viewHolder.mView.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick( final View view ) {

                        CharSequence options[] = new CharSequence[]{"Open Profile", "Send Message"};



                        AlertDialog.Builder builder = new AlertDialog.Builder ( UsersFragment.this.getActivity () );

                        builder.setTitle ( "Select Option" );

                        builder.setItems ( options, new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick( DialogInterface dialogInterface, int i ) {

                                if (i == 0) {

                                    Intent q = new Intent ( UsersFragment.this.getActivity (), NySpecificUserProfile.class );
                                    q.putExtra ( "userId", userId );
                                    startActivity ( q );

                                }

                                if (i == 1) {

                                    Intent c = new Intent ( UsersFragment.this.getActivity (), MessagesActivity.class );
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

        recyclerView.setAdapter ( firebaseRecyclerAdapter );
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
