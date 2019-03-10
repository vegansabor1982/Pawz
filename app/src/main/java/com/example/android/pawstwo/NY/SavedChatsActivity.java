package com.example.android.pawstwo.NY;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pawstwo.ChatVersionTwo.MessagesActivity;
import com.example.android.pawstwo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SavedChatsActivity extends AppCompatActivity {


    private RecyclerView mConvList;

    private DatabaseReference mConvDatabase;
    private DatabaseReference mMessageDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;



    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_saved_chats );



        //mMainView = inflater.inflate(R.layout.fragment_chats, container, false);



        mConvList =  findViewById(R.id.saved_chats_lists);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

      //  mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(mCurrent_user_id);


        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("ChatsTwo");
        mMessageDatabase.keepSynced(true);
        mUsersDatabase.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext ());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mConvList.setHasFixedSize(true);
        mConvList.setLayoutManager(linearLayoutManager);




    }


    @Override
    public void onStart() {
        super.onStart();

        Query conversationQuery = mMessageDatabase.child ( "message" ).orderByChild("timestamp");

        FirebaseRecyclerOptions<NyUsers> options = new FirebaseRecyclerOptions.Builder<NyUsers> ().setQuery ( conversationQuery, NyUsers.class ).build ();

        final FirebaseRecyclerAdapter<NyUsers, ConvViewHolder> firebaseConvAdapter = new FirebaseRecyclerAdapter<NyUsers, ConvViewHolder> ( options ) {
            @Override
            protected void onBindViewHolder( @NonNull final ConvViewHolder holder, int position, @NonNull final NyUsers model ) {


                final String list_user_id = getRef(position).getKey();

                Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);

                lastMessageQuery.addChildEventListener(new ChildEventListener () {
                    @Override
                    public void onChildAdded( DataSnapshot dataSnapshot, String s) {

                        String data = dataSnapshot.child("message").getValue().toString();
                          holder.setMessage(data, true);

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener () {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("userName").getValue().toString();
//                        String userThumb = dataSnapshot.child("thumb_image").getValue().toString();
                       String userImage = dataSnapshot.child ( "imageUrl" ).getValue ().toString ();



                        holder.setName(userName);
                        holder.setUserImage ( getApplicationContext (), userImage );

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                Intent chatIntent = new Intent(SavedChatsActivity.this , MessagesActivity.class);
                                chatIntent.putExtra("userId", list_user_id);
                                chatIntent.putExtra("userName", model.getUserName ());
                                startActivity(chatIntent);

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });




            }


            @NonNull
            @Override
            public ConvViewHolder onCreateViewHolder( @NonNull ViewGroup viewGroup, int i ) {
                View view = LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.ny_users_single_layout, viewGroup, false );

                return new ConvViewHolder ( view );
            }



        };
        mConvList.setAdapter(firebaseConvAdapter);
        firebaseConvAdapter.startListening ();



    }

    public static class ConvViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ConvViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setMessage(String message, boolean isSeen){

            TextView userStatusView = (TextView) mView.findViewById(R.id.ny_user_sinlge_status);
            userStatusView.setText(message);

            if(!isSeen){
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.BOLD);
            } else {
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.NORMAL);
            }

        }

        public void setName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.ny_user_single_name);
            userNameView.setText(name);

        }

        public void setUserImage( Context ctx, String userImage){

            ImageView userImageView =  mView.findViewById(R.id.ny_user_single_image);
            Picasso.with(ctx).load(userImage).into(userImageView);

        }


    }
}