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

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter <UserAdapter.ViewHolder>{

    private Context mContext;

    private List<NyUsers> mUsers;

    public UserAdapter( Context mContext, List<NyUsers> mUsers, boolean b ){

        this.mUsers=mUsers;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {

        View view = LayoutInflater.from ( mContext ).inflate ( R.layout. ny_users_single_layout, parent, false);

        return new UserAdapter.ViewHolder ( view );


    }

    @Override
    public void onBindViewHolder( @NonNull ViewHolder holder, int position ) {

        final NyUsers nyUsers = mUsers.get ( position );

        holder.username.setText (nyUsers.getUserName ());





        if (nyUsers.getImage ().equals ( "default" )){
            holder.profile_image.setImageResource ( R.mipmap.ic_launcher );

        }else{

            Glide.with (mContext).load ( nyUsers.getImage ()).into ( holder.profile_image );


        }





    }

    @Override
    public int getItemCount() {
        return mUsers.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;

        public ViewHolder(View itemView){

            super(itemView);

            username=itemView.findViewById ( R.id. ny_user_single_name );
            profile_image=itemView.findViewById ( R.id.ny_user_single_image );
        }


    }

}
