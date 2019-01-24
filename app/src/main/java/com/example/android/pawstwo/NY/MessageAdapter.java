package com.example.android.pawstwo.NY;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pawstwo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageVieHolder> {

    private List<Messages> mMessageList;

    private FirebaseAuth mAuth;

    private DatabaseReference mUserDatabase;

    public MessageAdapter( List<Messages> mMessageList ) {

        this.mMessageList = mMessageList;
    }


    @NonNull
    @Override
    public MessageAdapter.MessageVieHolder onCreateViewHolder( @NonNull ViewGroup parent, int i ) {


        View v = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.ny_message_single_layout, parent, false );

        return new MessageVieHolder ( v );
    }




    public class MessageVieHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public TextView timeText;
        public TextView displayName;
        public ImageView messageImage;

        public CircleImageView profileImage;


        public MessageVieHolder( @NonNull View itemView ) {
            super ( itemView );

            messageText = itemView.findViewById ( R.id.message_text_layout );
            timeText = itemView.findViewById ( R.id.time_text_layout );
            profileImage = itemView.findViewById ( R.id.message_profile_layout );
            messageImage = itemView.findViewById ( R.id.message_image_layout );
        }





    }
    @Override
    public void onBindViewHolder( @NonNull final MessageAdapter.MessageVieHolder messageVieHolder, int i ) {

        //mAuth=FirebaseAuth.getInstance ();

        //String current_user_id = mAuth.getCurrentUser ().getUid ();

        final Messages c = mMessageList.get ( i );

        String from_user = c.getFrom ();
        String message_type = c.getType ();

        mUserDatabase = FirebaseDatabase.getInstance ().getReference ().child ( "Users" ).child ( from_user );


        mUserDatabase.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                String name = dataSnapshot.child ( "userName" ).getValue ().toString ();
                String image = dataSnapshot.child ( "imageUrl" ).getValue ().toString ();

                mAuth=FirebaseAuth.getInstance ();
//------------------------------FIX THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!--------------------------
              //  messageVieHolder.displayName.setText ( name );


                Picasso.with ( messageVieHolder.profileImage.getContext () ).load ( image )
                        .placeholder ( R.drawable.com_facebook_profile_picture_blank_portrait ).into ( messageVieHolder.profileImage );


            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );


        if (message_type.equals ( "text" )) {

            messageVieHolder.messageText.setText ( c.getMessage () );
            messageVieHolder.messageImage.setVisibility ( View.INVISIBLE );


        } else {

            messageVieHolder.messageText.setVisibility ( View.INVISIBLE );
            Picasso.with ( messageVieHolder.profileImage.getContext () ).load ( c.getMessage () )
                    .placeholder ( R.drawable.com_facebook_profile_picture_blank_portrait ).into ( messageVieHolder.messageImage );

        }


    }



    @Override
    public int getItemCount() {
        return mMessageList.size ();
    }
}
