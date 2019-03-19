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
import com.example.android.pawstwo.MainActivity;
import com.example.android.pawstwo.NY.NyUsers;
import com.example.android.pawstwo.Notifications.APIService;
import com.example.android.pawstwo.Notifications.Client;
import com.example.android.pawstwo.Notifications.Data;
import com.example.android.pawstwo.Notifications.MyResponse;
import com.example.android.pawstwo.Notifications.Sender;
import com.example.android.pawstwo.Notifications.Token;
import com.example.android.pawstwo.R;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesActivity extends AppCompatActivity {


    private CircleImageView profile_image;
    private TextView username;

    FirebaseUser fuser;

    DatabaseReference reference, notificationRef;
    Intent intent;
    private ImageButton btn_send;
    private EditText text_send;

    MessagesAdapter messagesAdapter;

    private  DatabaseReference mNotificationDatabase;

    List<ChatModel>mChat;

    RecyclerView recyclerView;

    private FirebaseAuth mAuth;

    APIService apiService;

    String userid;

    boolean notify=false;






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

               startActivity ( new Intent ( MessagesActivity.this, MainActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );

            }
        } );

        apiService= Client.getClient ("https://fcm.googleapis.com").create ( APIService.class );

        profile_image=findViewById ( R.id.profile_image_two  );
        username=findViewById ( R.id.username_two );
        btn_send=findViewById ( R.id.btn_send );
        text_send=findViewById ( R.id.text_send );
        recyclerView=findViewById ( R.id.messages_recycler_view );

        mNotificationDatabase=FirebaseDatabase.getInstance ().getReference ().child ( "notifications" );

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
                notify= true;
                String msg = text_send.getText ().toString ();
                if (!msg.equals ( "" )){

                    sendMessage ( fuser.getUid (), userid, msg );
                }else{

                    Toast.makeText ( MessagesActivity.this, "Message is Empty", Toast.LENGTH_SHORT ).show ();
                }

                text_send.getText ().clear ();
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

    private void sendMessage ( final String sender, final String receiver, final String message){




       /* HashMap<String, String> chatnotification = new HashMap<> (  );

        chatnotification.put ( "from", sender );
        chatnotification.put ( "message", message );
        notificationRef.child ( receiver ).push ().setValue ( chatnotification ).addOnCompleteListener ( new OnCompleteListener<Void> () {
            @Override
            public void onComplete( @NonNull Task<Void> task ) {

                if (task.isSuccessful ()){


                }

            }
        } );
*/
        HashMap<String, String> notificationData = new HashMap<> (  );
        notificationData.put ( "from", sender );
        notificationData.put ( "type", "text" );



        mNotificationDatabase.child ( fuser.getUid () ).push ().setValue ( notificationData ).addOnSuccessListener ( new OnSuccessListener<Void> () {
            @Override
            public void onSuccess( Void aVoid ) {

                DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();

                HashMap< String, Object> hashMap = new HashMap<> (  );

                hashMap.put ( "sender", sender );
                hashMap.put ( "receiver", receiver );
                hashMap.put ( "message", message );
                hashMap.put ( "timestamp", ServerValue.TIMESTAMP );


                reference.child ( "ChatsTwo" ).push ().setValue ( hashMap );

            }
        } );

        mAuth=FirebaseAuth.getInstance ();

        String currentUserId = mAuth.getCurrentUser ().getUid ();

        String deviceToken = FirebaseInstanceId.getInstance ().getToken (  );



        final DatabaseReference chatRef= FirebaseDatabase.getInstance ().getReference ("Chatlist").child(receiver).child ( sender );

        chatRef.addListenerForSingleValueEvent ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {


                if (!dataSnapshot.exists ()){

                    chatRef.child ( "id" ).setValue ( sender );
                }
            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );

        final String msg = message;

        reference= FirebaseDatabase.getInstance ().getReference ("Users").child ( fuser.getUid () );

        reference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                NyUsers nyUsers = dataSnapshot.getValue (NyUsers.class);
                if (notify) {

                   // sendNotification ( receiver, nyUsers.getUserName (), msg );

                }
                notify=false;

            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );




    }

    /*private void sendNotification( String receiver, final String username, final String message){

        DatabaseReference tokens = FirebaseDatabase.getInstance ().getReference ("Tokens");
        Query query = tokens.orderByKey ().equalTo ( receiver );
        query.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren ()){
                    Token token = snapshot.getValue (Token.class);
                    Data data = new Data ( fuser.getUid (), R.mipmap.ic_launcher,username+ ": "+message, "New Message", userid);

                    Sender sender = new Sender ( data, token.getToken () );

                    apiService.sendNotification ( sender ).enqueue ( new Callback<MyResponse> () {
                        @Override
                        public void onResponse( Call<MyResponse> call, Response<MyResponse> response ) {
                            
                            if (response.code ()==200){
                                if (response.body ().success==1){
                                    Toast.makeText ( MessagesActivity.this, "Failed", Toast.LENGTH_SHORT ).show ();
                                }
                            }

                            

                        }

                        @Override
                        public void onFailure( Call<MyResponse> call, Throwable t ) {

                        }
                    } );
                }

            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );
    }*/

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