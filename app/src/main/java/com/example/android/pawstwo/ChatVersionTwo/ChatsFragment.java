package com.example.android.pawstwo.ChatVersionTwo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.pawstwo.NY.NyUsers;
import com.example.android.pawstwo.Notifications.Data;
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


public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List< NyUsers> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<ChatList> userList;





    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {


        View view = inflater.inflate ( R.layout.fragment_chats, container, false );

        recyclerView=view.findViewById ( R.id.recycler_view_chats );
        recyclerView.setHasFixedSize ( true );
        recyclerView.setLayoutManager ( new LinearLayoutManager ( getContext () ) );

        fuser=FirebaseAuth.getInstance ().getCurrentUser ();
        userList=new ArrayList<> (  );


        reference=FirebaseDatabase.getInstance ().getReference ("Chatlist").child ( fuser.getUid () );

        reference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                userList.clear ();
                for (DataSnapshot snapshot: dataSnapshot.getChildren ()){

                    ChatList chatList = snapshot.getValue (ChatList.class);

                    userList.add ( chatList );
                }

                chatList();

            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );











        return view;
    }

    private void chatList() {


        mUsers=new ArrayList<> (  );

        reference=FirebaseDatabase.getInstance ().getReference ("Users");
        reference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                mUsers.clear ();

                for (DataSnapshot snapshot: dataSnapshot.getChildren ()){
                    NyUsers nyUsers=snapshot.getValue (NyUsers.class);
                    for (ChatList chatList: userList){

                        //------------this is the problem-----------------
                        if (nyUsers.getUserName ().equals ( chatList.getId () )){
                            //here ---------------------------------------------

                            mUsers.add ( nyUsers );
                        }

                    }


                }
                userAdapter = new UserAdapter ( getContext (),mUsers, true  );
                recyclerView.setAdapter ( userAdapter );

            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );
    }


}