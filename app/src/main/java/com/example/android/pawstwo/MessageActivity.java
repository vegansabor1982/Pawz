package com.example.android.pawstwo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MessageActivity extends AppCompatActivity {

   private static int SIGN_IN_REQUEST_CODE=1;
   private FirebaseListAdapter<ChatMessage> adapter;
   RelativeLayout activity_message;
   FloatingActionButton fab;



    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_message );

        fab=(FloatingActionButton) findViewById ( R.id.send_user_message );
        
        displayChatMessage();

        


    }

    private void displayChatMessage() {
    }
}
