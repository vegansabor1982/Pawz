package com.example.android.pawstwo.NY;


import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pawstwo.ProfileActivity;
import com.example.android.pawstwo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class NyFriendsfragment extends Fragment {

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;


    public NyFriendsfragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {

        mMainView = inflater.inflate ( R.layout.fragment_ny_friendsfragment, container, false );

        mFriendsList = ( RecyclerView ) mMainView.findViewById ( R.id.friends_list );
        mAuth = FirebaseAuth.getInstance ();

        mCurrent_user_id = mAuth.getCurrentUser ().getUid ();

        mFriendsDatabase = FirebaseDatabase.getInstance ().getReference ().child ( "Friends" ).child ( mCurrent_user_id );
        mFriendsDatabase.keepSynced ( true );
        mUsersDatabase = FirebaseDatabase.getInstance ().getReference ().child ( "Users" );
        mUsersDatabase.keepSynced ( true );


        mFriendsList.setHasFixedSize ( true );
        mFriendsList.setLayoutManager ( new LinearLayoutManager ( getContext () ) );


        return mMainView;


    }

    @Override
    public void onStart() {
        super.onStart ();

        Query query = FirebaseDatabase.getInstance ()
                .getReference ()
                .child ( "Friends" );

        FirebaseRecyclerOptions<NyFriends> options = new FirebaseRecyclerOptions.Builder<NyFriends> ().setQuery ( query, NyFriends.class ).build ();

        final FirebaseRecyclerAdapter<NyFriends, NyFriendsViewHolder> nyfriendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<NyFriends, NyFriendsViewHolder> ( options ) {

            @NonNull
            @Override
            public NyFriendsViewHolder onCreateViewHolder( @NonNull ViewGroup viewGroup, int i ) {
                return null;
            }

            @Override
            protected void onBindViewHolder( @NonNull NyFriendsViewHolder friendsViewHolder, int position, @NonNull NyFriends friends ) {


                friendsViewHolder.setDate (friends.getDate ());
            }
        };

        mFriendsList.setAdapter(nyfriendsRecyclerViewAdapter);
        nyfriendsRecyclerViewAdapter.startListening ();
    }


    public class NyFriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public NyFriendsViewHolder( View itemView ) {
            super ( itemView );

            mView = itemView;

        }

        public void setDate( String date ) {

            TextView userStatusView = mView.findViewById ( R.id.ny_user_sinlge_status );
            userStatusView.setText ( date );
        }

    }


}