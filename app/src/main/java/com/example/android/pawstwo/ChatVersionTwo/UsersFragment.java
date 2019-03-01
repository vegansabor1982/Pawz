package com.example.android.pawstwo.ChatVersionTwo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.pawstwo.NY.NyUsers;
import com.example.android.pawstwo.NY.SearchUsersActivity;
import com.example.android.pawstwo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;

    private List<NyUsers> mUsers;

    private Button mSearchBtn;


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View view = inflater.inflate ( R.layout.fragment_users, container, false );


        mSearchBtn=view.findViewById ( R.id.btn_search_username );

        mSearchBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View view ) {
                Intent y = new Intent (view.getContext (), SearchUsersActivity.class );

                startActivity ( y );
            }
        } );





        recyclerView = view.findViewById ( R.id.users_recycler_view );
        recyclerView.setHasFixedSize ( true );
        recyclerView.setLayoutManager ( new LinearLayoutManager ( getContext () ) );

        mUsers = new ArrayList<> ();

        readUsers ();


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
}
