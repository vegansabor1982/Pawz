package com.example.android.pawstwo.ChatVersionTwo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.android.pawstwo.NY.NyUsers;
import com.example.android.pawstwo.R;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesActivity extends AppCompatActivity {


    private CircleImageView profile_image;
    private TextView username;

    FirebaseUser fuser;

    DatabaseReference reference;
    Intent intent;
    private ImageButton btn_send;
    private EditText text_send;

    MessagesAdapter messagesAdapter;

    List<ChatModel>mChat;

    RecyclerView recyclerView;





    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_messages );








        Toolbar toolbar = findViewById ( R.id.toolbarthree );
        setSupportActionBar ( toolbar );
        getSupportActionBar ().setTitle ( "" );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

        toolbar.setNavigationOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View view ) {

                finish ();

            }
        } );

        profile_image=findViewById ( R.id.profile_image_two  );
        username=findViewById ( R.id.username_two );
        btn_send=findViewById ( R.id.btn_send );
        text_send=findViewById ( R.id.text_send );
        recyclerView=findViewById ( R.id.messages_recycler_view );

        recyclerView.setHasFixedSize ( true );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager ( getApplicationContext () );
        linearLayoutManager.setStackFromEnd ( true );
        recyclerView.setLayoutManager ( linearLayoutManager );


        intent=getIntent ();

        final String userid=intent.getStringExtra ("userId"  );



        fuser= FirebaseAuth.getInstance ().getCurrentUser ();


        btn_send.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View view ) {
                String msg = text_send.getText ().toString ();
                if (!msg.equals ( "" )){

                    sendMessage ( fuser.getUid (), userid, msg );
                }else{

                    Toast.makeText ( MessagesActivity.this, "Message is Empty", Toast.LENGTH_SHORT ).show ();
                }

                text_send.setText ( " " );
            }
        } );


        reference=FirebaseDatabase.getInstance ().getReference ("Users").child ( userid );

        reference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                NyUsers user =dataSnapshot.getValue (NyUsers.class);
                username.setText ( user.getUserName () );

                if (user.getImage ().equals ( "default" )){

                    profile_image.setImageResource ( R.mipmap.ic_launcher );


                }else{
                    Glide.with (MessagesActivity.this).load(user.getImage ()).into ( profile_image );


                }

                readMessages ( fuser.getUid (), userid, user.getImage () );

            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );
    }

    private void sendMessage (String sender, String receiver, String message){


        DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();

        HashMap< String, Object> hashMap = new HashMap<> (  );

        hashMap.put ( "sender", sender );
        hashMap.put ( "receiver", receiver );
        hashMap.put ( "message", message );
        hashMap.put ( "timestamp", ServerValue.TIMESTAMP );


        reference.child ( "ChatsTwo" ).push ().setValue ( hashMap );



        final DatabaseReference chatRef= FirebaseDatabase.getInstance ().getReference ("Chatlist").child(receiver).child ( fuser.getUid () );

        chatRef.addListenerForSingleValueEvent ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {


                if (!dataSnapshot.exists ()){

                    chatRef.child ( "id" ).setValue ( fuser.getUid () );
                }
            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );


    }

    private void readMessages ( final String myid, final String userid, final String imageurl){

        mChat=new ArrayList<> (  );
        reference=FirebaseDatabase.getInstance ().getReference ("ChatsTwo");

        reference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                mChat.clear ();
                for (DataSnapshot snapshot: dataSnapshot.getChildren ()){

                    ChatModel chatModel =snapshot.getValue (ChatModel.class);
                    if(chatModel.getReceiver ().equals ( myid )&& chatModel.getSender ().equals ( userid )|| chatModel.getReceiver ().equals ( userid )&& chatModel.getSender ().equals ( myid )){

                        mChat.add ( chatModel );
                    }

                    messagesAdapter = new MessagesAdapter ( MessagesActivity.this,mChat, imageurl  );

                    recyclerView.setAdapter ( messagesAdapter );
                    recyclerView.scrollToPosition (mChat.size ()-1);
                }

            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );
    }
}
