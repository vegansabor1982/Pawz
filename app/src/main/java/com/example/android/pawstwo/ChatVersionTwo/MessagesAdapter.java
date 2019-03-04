package com.example.android.pawstwo.ChatVersionTwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.pawstwo.NY.NyUsers;
import com.example.android.pawstwo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter <MessagesAdapter.ViewHolder>{

    private Context mContext;

    private List<ChatModel> mChat;

    private String imageurl;

    FirebaseUser fuser;

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;


    public MessagesAdapter (Context mContext, List<ChatModel> mChat,String imageurl ){

        this.mChat=mChat;
        this.mContext=mContext;
        this.imageurl=imageurl;
    }

    @NonNull
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {

        if (viewType==MSG_TYPE_RIGHT){
            View view = LayoutInflater.from ( mContext ).inflate ( R.layout. chat_item_right, parent, false);

            return new MessagesAdapter.ViewHolder ( view );
        }else{

            View view = LayoutInflater.from ( mContext ).inflate ( R.layout. chat_item_left, parent, false);

            return new MessagesAdapter.ViewHolder ( view );


        }




    }

    @Override
    public void onBindViewHolder( @NonNull MessagesAdapter.ViewHolder holder, int position ) {

        ChatModel chatModel = mChat.get ( position );

        holder.show_message.setText ( chatModel.getMessage () );

        if (imageurl.equals ( "default" )){
            holder.profile_image.setImageResource ( R.mipmap.ic_launcher );
        }else{
           Glide.with ( mContext ).load ( imageurl ).into ( holder.profile_image );

        }






    }

    @Override
    public int getItemCount() {
        return mChat.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(View itemView){

            super(itemView);

            show_message=itemView.findViewById ( R.id. show_message );
            profile_image=itemView.findViewById ( R.id.chat_profile_image );
        }


    }

    @Override
    public int getItemViewType( int position ) {
        fuser=FirebaseAuth.getInstance ().getCurrentUser ();
        if (mChat.get ( position ).getSender ().equals ( fuser.getUid () )){

            return MSG_TYPE_RIGHT;
        }else{

            return MSG_TYPE_LEFT;
        }
    }
}

