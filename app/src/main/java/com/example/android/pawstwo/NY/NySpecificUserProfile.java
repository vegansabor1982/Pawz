package com.example.android.pawstwo.NY;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pawstwo.ProfileActivity;
import com.example.android.pawstwo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NySpecificUserProfile extends AppCompatActivity {

    private ImageView mProfileImage;
    private TextView mProfileName, mProfileFriendsCount;
    private Button mProfileSendRequestBtn;
    private Button mDeclineBtn;

    private DatabaseReference mUsersDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mFriendRequestDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase;
    private DatabaseReference mRootRef;


    private FirebaseUser mCurrentUser;

    private String mCurrent_state;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_ny_specific_user_profile );


       final String user_id;
       String data =getIntent ().getStringExtra ("userId"  );
        if (data == null) {
           user_id=getIntent ().getStringExtra ( "from_user_id" );

        }else{
            user_id=getIntent ().getStringExtra ( "userId" );
        }

       mAuth=FirebaseAuth.getInstance ();

       mUsersDatabase=FirebaseDatabase.getInstance ().getReference ().child ( "Users" ).child ( user_id );
       mFriendRequestDatabase= FirebaseDatabase.getInstance ().getReference ().child ( "Friend_req" );
       mFriendDatabase=FirebaseDatabase.getInstance ().getReference ().child ( "Friends" );
       mNotificationDatabase=FirebaseDatabase.getInstance ().getReference ().child ( "notifications" );
       mCurrentUser=FirebaseAuth.getInstance ().getCurrentUser ();
       mRootRef=FirebaseDatabase.getInstance ().getReference ();


        mProfileImage = findViewById ( R.id.profile_image );
        mProfileName=findViewById ( R.id.ny_tv_specific_profile_name );
        mProfileFriendsCount=findViewById ( R.id. profile_totalFriends);
        mProfileSendRequestBtn=findViewById ( R.id.profile_send_req_btn);
        mDeclineBtn=findViewById ( R.id. profile_decline_btn );

        mCurrent_state = "Not Friends";

        mDeclineBtn.setVisibility ( View.INVISIBLE );
        mDeclineBtn.setEnabled ( false );




        mProgressDialog = new ProgressDialog ( this );
        mProgressDialog.setTitle ( "Loading User Data" );
        mProgressDialog.setMessage ( "Please Wait" );
        mProgressDialog.setCanceledOnTouchOutside ( false );
        mProgressDialog.show ();



        mUsersDatabase.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                String display_name=dataSnapshot.child ( "userName" ).getValue ().toString ();
                String image=dataSnapshot.child ( "imageUrl" ).getValue ().toString ();

                mProfileName.setText ( display_name );

                Picasso.with ( NySpecificUserProfile.this ).load ( image ).placeholder ( R.drawable.com_facebook_profile_picture_blank_portrait ).into(mProfileImage);


                //-------------------FRIENDS LIST/ REQUEST ---------------------------

                mFriendRequestDatabase.child ( mCurrentUser.getUid () ).addListenerForSingleValueEvent ( new ValueEventListener () {
                    @Override
                    public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                        if (dataSnapshot.hasChild ( user_id )){
                            String req_type=dataSnapshot.child ( user_id ).child ( "request_type" ).getValue ().toString ();

                            if (req_type.equals ("received"  )){



                                mCurrent_state="req_received";
                                mProfileSendRequestBtn.setText ( "Accept Friend Request" );

                                mDeclineBtn.setVisibility ( View.VISIBLE );
                                mDeclineBtn.setEnabled ( true );

                            }else if(req_type.equals ( "sent" )){

                                mCurrent_state = "req_sent";
                                mProfileSendRequestBtn.setText ( " Cancel Friend Request" );

                                mDeclineBtn.setVisibility ( View.INVISIBLE );
                                mDeclineBtn.setEnabled ( false );


                            }else {

                                mFriendDatabase.child ( mCurrentUser.getUid () ).addListenerForSingleValueEvent ( new ValueEventListener () {
                                    @Override
                                    public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                                        if (dataSnapshot.hasChild ( user_id )){

                                            mCurrent_state="friends";
                                            mProfileSendRequestBtn.setText ( "Unfriend " );

                                            mDeclineBtn.setVisibility ( View.INVISIBLE );
                                            mDeclineBtn.setEnabled ( false );


                                        }
                                    }

                                    @Override
                                    public void onCancelled( @NonNull DatabaseError databaseError ) {

                                    }
                                } );
                            }
                        }

                        mProgressDialog.dismiss ();

                    }

                    @Override
                    public void onCancelled( @NonNull DatabaseError databaseError ) {

                    }
                } );






            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );

        mProfileSendRequestBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View view ) {

                mProfileSendRequestBtn.setEnabled ( false );

                //--------------------------------------NOT FRIENDS STATE--------------------------------
                if (mCurrent_state.equals ( "Not Friends" )){


                    DatabaseReference newNotificationRef=mRootRef.child ( "notifications" ).child ( user_id ).push ();
                    String newNotificationId=newNotificationRef.getKey ();

                    HashMap<String, String> notificationData=new HashMap<> (  );
                    notificationData.put ( "from", mCurrentUser.getUid () );
                    notificationData.put ( "type", "request" );


                    Map requestMap=new HashMap (  );
                    requestMap.put ("Friend_req/"+ mCurrentUser.getUid () + "/" + user_id+ "/request_type", "sent");
                    requestMap.put ( "Friend_req/" + user_id + "/" + mCurrentUser.getUid ()+"/request_type", "received");
                    requestMap.put ( "notifications/"+user_id+ "/"+newNotificationId, notificationData );

                    mRootRef.updateChildren ( requestMap, new DatabaseReference.CompletionListener () {
                        @Override
                        public void onComplete( @Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference ) {

                          if (databaseError!= null){

                              Toast.makeText ( NySpecificUserProfile.this, "There was some error in sending request",Toast.LENGTH_SHORT ).show ();
                          }


                          mProfileSendRequestBtn.setEnabled ( true );

                            mCurrent_state="req_sent";
                            mProfileSendRequestBtn.setText ( "Cancel Friend Request" );







                        }
                    } );


                }

                //-------------CANCEL FRIEND REQUEST-------------------

                if (mCurrent_state.equals ( "req_sent" )){

                    mFriendRequestDatabase.child ( mCurrentUser.getUid () ).child ( user_id ).removeValue ().addOnSuccessListener ( new OnSuccessListener<Void> () {
                        @Override
                        public void onSuccess( Void aVoid ) {

                            mFriendRequestDatabase.child ( user_id ).child ( mCurrentUser.getUid () ).removeValue ().addOnSuccessListener ( new OnSuccessListener<Void> () {
                                @Override
                                public void onSuccess( Void aVoid ) {

                                    mProfileSendRequestBtn.setEnabled ( true );


                                    mCurrent_state="Not Friends";
                                    mProfileSendRequestBtn.setText ( "Send Friend Request" );

                                    mDeclineBtn.setVisibility ( View.INVISIBLE );
                                    mDeclineBtn.setEnabled ( false );



                                }
                            } );

                        }
                    } );

                }



                //-----------------REQ RECEIVED STATE---------------------

                if(mCurrent_state.equals("req_received")){

                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                    Map friendsMap = new HashMap();
                    friendsMap.put("Friends/" + mCurrentUser.getUid() + "/" + user_id + "/date", currentDate);
                    friendsMap.put("Friends/" + user_id + "/"  + mCurrentUser.getUid() + "/date", currentDate);


                    friendsMap.put("Friend_req/" + mCurrentUser.getUid() + "/" + user_id, null);
                    friendsMap.put("Friend_req/" + user_id + "/" + mCurrentUser.getUid(), null);


                    mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                            if(databaseError == null){

                                mProfileSendRequestBtn.setEnabled(true);
                                mCurrent_state = "friends";
                                mProfileSendRequestBtn.setText("Unfriend this Person");

                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                mDeclineBtn.setEnabled(false);

                            } else {

                                String error = databaseError.getMessage();

                                Toast.makeText(NySpecificUserProfile.this, error, Toast.LENGTH_SHORT).show();


                            }

                        }
                    });

                }

                //-----------------------UNFRRIEND--------------

                if (mCurrent_state.equals ( "friends" )){

                    Map unfriendMap= new HashMap (  );
                    unfriendMap.put ( "Friends/" + mCurrentUser.getUid () + "/" + user_id, null );
                    unfriendMap.put ( "Friends/" + user_id + "/" + mCurrentUser.getUid (), null);

                    mRootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                            if(databaseError == null){


                                mCurrent_state = "Not Friends";
                                mProfileSendRequestBtn.setText("Send Friend Request");

                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                mDeclineBtn.setEnabled(false);

                            } else {

                                String error = databaseError.getMessage();

                                Toast.makeText(NySpecificUserProfile.this, error, Toast.LENGTH_SHORT).show();


                            }
                            mProfileSendRequestBtn.setEnabled(true);

                        }
                    });
                }

                //--------------DECLINE FRIEND REQUEST----------------------
            }




        } );


    }
}
