package com.example.android.pawstwo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import android.text.format.DateFormat;

//import java.text.DateFormat;

public class MessageActivity extends AppCompatActivity {

   private static int SIGN_IN_REQUEST_CODE=1;
   private FirebaseListAdapter<ChatMessage> adapter;
   RelativeLayout activity_message;
   FloatingActionButton fab;
   private FirebaseDatabase mFirebaseDatabase;

    @Override
    protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data ) {
        super.onActivityResult ( requestCode, resultCode, data );


    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_message );

        fab=(FloatingActionButton) findViewById ( R.id.send_user_message );
        
        displayChatMessage();

        fab.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View view ) {
                EditText input = findViewById ( R.id.input );
                FirebaseDatabase.getInstance ().getReference (  ).child ( "chats" ).push ().setValue ( new ChatMessage ( input.getText ().toString (),FirebaseAuth.getInstance ().getCurrentUser ().getEmail ()  ) );

               input.setText ( " " );
               BubbleTextView bubbleTextView= findViewById ( R.id.message_text );
               bubbleTextView.setText ( " " );
            }
        } );

        


    }

    private void displayChatMessage() {

       ListView listOfMessages = findViewById ( R.id. list_of_messages );


        Query query = FirebaseDatabase.getInstance().getReference().child ( "chats" ) ;
        FirebaseApp.initializeApp (this);

        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .setLayout(R.layout.chat_list_item)
                .build();

        adapter=new FirebaseListAdapter<ChatMessage> (options) {

            @Override
            protected void populateView( @NonNull View v, @NonNull ChatMessage model, int position ) {

                TextView messageText,messageUser,messageTime;
                messageText=v.findViewById ( R.id.message_text );
                messageTime=v.findViewById ( R.id.message_time );
                messageUser=v.findViewById ( R.id.message_user );

                messageText.setText ( model.getMessageText () );
                messageUser.setText ( model.getMessageUser () );
                messageTime.setText (DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime () ) );

            }
        };

        listOfMessages.setAdapter ( adapter );
        adapter.startListening ();



    }
}
