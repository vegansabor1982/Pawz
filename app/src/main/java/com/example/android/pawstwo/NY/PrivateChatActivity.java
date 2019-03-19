package com.example.android.pawstwo.NY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.pawstwo.Notifications.APIService;
import com.example.android.pawstwo.Notifications.APIService;
import com.example.android.pawstwo.Notifications.Client;
import com.example.android.pawstwo.Notifications.Data;
import com.example.android.pawstwo.Notifications.MyResponse;
import com.example.android.pawstwo.Notifications.Sender;
import com.example.android.pawstwo.Notifications.Token;
import com.example.android.pawstwo.R;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivateChatActivity extends AppCompatActivity {


    private String mChatUser;

    private Toolbar mChatToolbar;

    private DatabaseReference mRootRef;

    private FirebaseAuth mAuth;

    private TextView mTitleView;
    private CircleImageView mProfileImage;

    private String mCurrentUserId;

    private ImageButton mChatAddBtn;
    private ImageButton mChatSendBtn;
    private EditText mChatMessageView;

    private RecyclerView mMessagesList;

    private SwipeRefreshLayout mRefreshLayout;

    private final List<Messages> messagesList = new ArrayList<> ();
    private LinearLayoutManager mLinearLayout;

    private MessageAdapter mAdapter;

    private DatabaseReference mMessageDatabase;

    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private int mCurrentPage = 1;

    private StorageReference mImageStorage;

    private int itemPos = 0;

    private String mLastKey = " ";
    private String mPrevKey = " ";

    private static final int GALLERY_PICK = 1;

    private FirebaseUser fuser;

    APIService apiService;

    boolean notify = false;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_private_chat );


        mChatToolbar = findViewById ( R.id.chat_app_bar );


        setSupportActionBar ( mChatToolbar );

        ActionBar actionBar = getSupportActionBar ();


        actionBar.setDisplayHomeAsUpEnabled ( true );

        actionBar.setDisplayShowCustomEnabled ( true );

        mRootRef = FirebaseDatabase.getInstance ().getReference ();

        mAuth = FirebaseAuth.getInstance ();
        mCurrentUserId = mAuth.getCurrentUser ().getUid ();


        mChatUser = getIntent ().getStringExtra ( "userId" );
        String userName = getIntent ().getStringExtra ( "userName" );

        //getSupportActionBar ().setTitle ( userName );


        LayoutInflater inflater = ( LayoutInflater ) this.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );

        View action_bar_view = inflater.inflate ( R.layout.ny_chat_custom_bar, null );

        actionBar.setCustomView ( action_bar_view );


        apiService = Client.getClient ( "https://fcm.googleapis.com/" ).create ( APIService.class );

        //--------------Custom action bar items--------------

        mTitleView = findViewById ( R.id.custom_bar_title );
        mProfileImage = findViewById ( R.id.custom_bar_image );

        mChatAddBtn = findViewById ( R.id.chat_add_btn );
        mChatSendBtn = findViewById ( R.id.chat_send_btn );
        mChatMessageView = findViewById ( R.id.chat_message_view );

        mAdapter = new MessageAdapter ( messagesList );

        mMessagesList = findViewById ( R.id.messages_list );
        mRefreshLayout = ( SwipeRefreshLayout ) findViewById ( R.id.message_swipe_layout );
//-----------------maybe here context has problem------------->>
        mLinearLayout = new LinearLayoutManager ( this );
        mMessagesList.setHasFixedSize ( true );
        mMessagesList.setLayoutManager ( mLinearLayout );

        mMessagesList.setAdapter ( mAdapter );


        //------------Image storage----------------

        mImageStorage = FirebaseStorage.getInstance ().getReference ();
        mRootRef.child ( "Chat" ).child ( mCurrentUserId ).child ( mChatUser ).child ( "seen" ).setValue ( true );


        mTitleView.setText ( userName );

        mRootRef.child ( "Users" ).child ( mChatUser ).addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                String image = dataSnapshot.child ( "imageUrl" ).getValue ().toString ();

                Picasso.with ( getApplicationContext () ).load ( image ).fit ().centerCrop ().into ( mProfileImage );

            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );

        //----

        mRootRef.child ( "Chat" ).child ( mCurrentUserId ).addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                if (!dataSnapshot.hasChild ( mChatUser )) {

                    Map chatAddMap = new HashMap ();
                    chatAddMap.put ( "seen", false );
                    chatAddMap.put ( "timestamp", ServerValue.TIMESTAMP );


                    Map chatUserMap = new HashMap ();

                    chatUserMap.put ( "Chat/" + mCurrentUserId + "/" + mChatUser, chatAddMap );
                    chatUserMap.put ( "Chat/" + mChatUser + "/" + mCurrentUserId, chatAddMap );

                    mRootRef.updateChildren ( chatUserMap, new DatabaseReference.CompletionListener () {
                        @Override
                        public void onComplete( @Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference ) {

                            if (databaseError != null) {

                                Log.d ( "CHAT_LOG", databaseError.getMessage ().toString () );
                            }


                        }
                    } );

                }

            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );


        mChatSendBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View view ) {

                notify = true;


                sendMessage ();


            }
        } );

        mChatAddBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View view ) {

                Intent galleryIntent = new Intent ();
                galleryIntent.setType ( "image/*" );
                galleryIntent.setAction ( Intent.ACTION_GET_CONTENT );

                startActivityForResult ( Intent.createChooser ( galleryIntent, "SELECT IMAGE" ), GALLERY_PICK );

            }
        } );

        mRefreshLayout.setOnRefreshListener ( new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh() {
                mCurrentPage++;

                messagesList.clear ();

                itemPos = 0;

                loadMoreMessages ();


            }
        } );

        loadMessages ();
        loadMessagesTwo ();


    }

    @Override
    public void onBackPressed() {


        super.onBackPressed ();
    }

    @Override
    protected void onSaveInstanceState( Bundle outState ) {
        super.onSaveInstanceState ( outState );
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult ( requestCode, resultCode, data );

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData ();

            final String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
            final String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

            DatabaseReference user_message_push = mRootRef.child ( "messages" )
                    .child ( mCurrentUserId ).child ( mChatUser ).push ();

            final String push_id = user_message_push.getKey ();


        }
    }


    private void loadMoreMessages() {

        DatabaseReference messageRef = mRootRef.child ( "messages" ).child ( mChatUser ).child ( mCurrentUserId );

        Query messageQuery = messageRef.orderByKey ().endAt ( mLastKey ).limitToLast ( 10 );

        messageQuery.addChildEventListener ( new ChildEventListener () {
            @Override
            public void onChildAdded( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {

                Messages message = dataSnapshot.getValue ( Messages.class );

                String messageKey = dataSnapshot.getKey ();

                if (!mPrevKey.equals ( messageKey )) {

                    messagesList.add ( itemPos++, message );

                } else {

                    mPrevKey = mLastKey;

                }


                if (itemPos == 1) {

                    mLastKey = messageKey;

                }

                Log.d ( "TOTALKEYS", "Last Key : " + mLastKey + " | Prev Key : " + mPrevKey + " | Message Key : " + messageKey );


                messagesList.add ( message );
                mAdapter.notifyDataSetChanged ();

                mMessagesList.scrollToPosition ( messagesList.size () - 1 );
                mRefreshLayout.setRefreshing ( false );
                mLinearLayout.scrollToPositionWithOffset ( 10, 0 );

            }

            @Override
            public void onChildChanged( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {

            }

            @Override
            public void onChildRemoved( @NonNull DataSnapshot dataSnapshot ) {

            }

            @Override
            public void onChildMoved( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {

            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );


    }

    private void loadMessages() {
//--------------------------------------------------------------------------TRY to change these two>>-----------------
        DatabaseReference messageRef = mRootRef.child ( "messages" ).child ( mChatUser ).child ( mCurrentUserId );

        Query messageQuery = messageRef.limitToLast ( mCurrentPage * TOTAL_ITEMS_TO_LOAD );


        messageQuery.addChildEventListener ( new ChildEventListener () {
            @Override
            public void onChildAdded( DataSnapshot dataSnapshot, String s ) {

                Messages message = dataSnapshot.getValue ( Messages.class );

                //  long time = dataSnapshot.getValue (Messages.class).getTime ();

                itemPos++;

                if (itemPos == 1) {

                    String messageKey = dataSnapshot.getKey ();

                    mLastKey = messageKey;
                    mPrevKey = messageKey;

                }

                messagesList.add ( message );

                mAdapter.notifyDataSetChanged ();

                mMessagesList.scrollToPosition ( messagesList.size () - 1 );

                mRefreshLayout.setRefreshing ( false );

            }

            @Override
            public void onChildChanged( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {

            }

            @Override
            public void onChildRemoved( @NonNull DataSnapshot dataSnapshot ) {

            }

            @Override
            public void onChildMoved( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {

            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );


    }

    private void loadMessagesTwo() {
//--------------------------------------------------------------------------TRY to change these two>>-----------------
        DatabaseReference messageRef = mRootRef.child ( "messages" ).child ( mCurrentUserId ).child ( mChatUser );

        Query messageQuery = messageRef.limitToLast ( mCurrentPage * TOTAL_ITEMS_TO_LOAD );


        messageQuery.addChildEventListener ( new ChildEventListener () {
            @Override
            public void onChildAdded( DataSnapshot dataSnapshot, String s ) {

                Messages message = dataSnapshot.getValue ( Messages.class );

                // long time = dataSnapshot.getValue (Messages.class).getTime ();

                itemPos++;

                if (itemPos == 1) {

                    String messageKey = dataSnapshot.getKey ();

                    mLastKey = messageKey;
                    mPrevKey = messageKey;

                }

                messagesList.add ( message );
                mAdapter.notifyDataSetChanged ();

                mMessagesList.scrollToPosition ( messagesList.size () - 1 );

                mRefreshLayout.setRefreshing ( false );

            }

            @Override
            public void onChildChanged( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {

            }

            @Override
            public void onChildRemoved( @NonNull DataSnapshot dataSnapshot ) {

            }

            @Override
            public void onChildMoved( @NonNull DataSnapshot dataSnapshot, @Nullable String s ) {

            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );


    }


    private void sendMessage() {

        String message = mChatMessageView.getText ().toString ();

        // final String msg = message;


        if (!TextUtils.isEmpty ( message )) {

            String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
            String chat_user_ref = "messages/" + mChatUser + "/ " + mCurrentUserId;

            DatabaseReference user_message_push = mRootRef.child ( "messages" ).child ( mCurrentUserId ).child ( mChatUser ).push ();

            String push_id = user_message_push.getKey ();

            Map messageMap = new HashMap ();

            messageMap.put ( "message", message );
            messageMap.put ( "seen", false );
            messageMap.put ( "type", "text" );
            messageMap.put ( "time", ServerValue.TIMESTAMP );
            messageMap.put ( "from", mCurrentUserId );


            Map messageUserMap = new HashMap ();
            messageUserMap.put ( current_user_ref + "/" + push_id, messageMap );
            messageUserMap.put ( chat_user_ref + "/" + push_id, messageMap );

            mChatMessageView.setText ( " " );

            mRootRef.child ( "Chat" ).child ( mCurrentUserId ).child ( mChatUser ).child ( "seen" ).setValue ( true );
            mRootRef.child ( "Chat" ).child ( mCurrentUserId ).child ( mChatUser ).child ( "timestamp" ).setValue ( ServerValue.TIMESTAMP );

            mRootRef.child ( "Chat" ).child ( mChatUser ).child ( mCurrentUserId ).child ( "seen" ).setValue ( false );
            mRootRef.child ( "Chat" ).child ( mChatUser ).child ( mCurrentUserId ).child ( "timestamp" ).setValue ( ServerValue.TIMESTAMP );

            mRootRef.updateChildren ( messageUserMap, new DatabaseReference.CompletionListener () {
                @Override
                public void onComplete( @Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference ) {


                    if (databaseError != null) {

                        Log.d ( "CHAT_LOG", databaseError.getMessage ().toString () );


                    }

                }
            } );


            //   mRootRef = FirebaseDatabase.getInstance ().getReference ( "Users" ).child ( mAuth.getCurrentUser ().getUid () );


        }


    }


    private void sendNotification( String receiver, final String username, final String message ) {

        DatabaseReference tokens = FirebaseDatabase.getInstance ().getReference ( "Tokens" );

        Query query = tokens.orderByKey ().equalTo ( receiver );

        query.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren ()) {

                    Token token = snapshot.getValue ( Token.class );
                    Data data = new Data ( fuser.getUid (), R.drawable.com_facebook_profile_picture_blank_portrait, username + ": " + message, "New Message", mCurrentUserId );

                    Sender sender = new Sender ( data, token.getToken () );

                    apiService.sendNotification ( sender ).enqueue ( new Callback<MyResponse> () {
                        @Override
                        public void onResponse( Call<MyResponse> call, Response<MyResponse> response ) {

                            if (response.code () == 200) {

                                if (response.body ().success != 1) {

                                    Toast.makeText ( PrivateChatActivity.this, "Failed", Toast.LENGTH_SHORT ).show ();
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
    }

    private void updateToken( String token ) {

        fuser = FirebaseAuth.getInstance ().getCurrentUser ();


        DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ( "Tokens" );

        Token token1 = new Token ( token );

        reference.child ( fuser.getUid () ).setValue ( token1 );


    }

}

