package com.example.android.pawstwo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.pawstwo.NY.FavsTwo;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PetAdapterTestThree extends RecyclerView.Adapter<PetAdapterTestThree.PetHolder> {

    private Context mContext;
    private List <FavsTwo> mUploadTest;
    private OnItemClickListener mListener;
    private FirebaseAuth mAuth;

    public interface OnItemClickListener {

        void onItemClick(int position);


    }

    public void setOnItemClickListener( OnItemClickListener listener ){

        mListener=listener;
    }

    public PetAdapterTestThree (Context context,List<FavsTwo> uploads){

        mContext=context;
        mUploadTest=uploads;
    }

    @NonNull
    @Override
    public PetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from (mContext).inflate ( R.layout.image_item_test_three,parent, false );
        return new PetHolder ( v );
    }

    @Override
    public void onBindViewHolder( PetHolder petHolder, int position) {

        FavsTwo uploadCurrent = mUploadTest.get ( position );

        petHolder.textViewFamily.setText ( uploadCurrent.getmFamily () );
        petHolder.textViewType.setText ( uploadCurrent.getmType () );
        petHolder.textViewDescription.setText ( uploadCurrent.getmDescription () );
        petHolder.mLatReturn.setText ( uploadCurrent.getmLat () );
        petHolder.mLongReturn.setText ( uploadCurrent.getmLong () );

        petHolder.mUploaderNew.setText ( uploadCurrent.getmUploader ());


        Picasso.with (mContext).load ( uploadCurrent.getmImageUrl () ).fit ().centerCrop ().into ( petHolder.imageView );








    }

    @Override
    public int getItemCount() {
        return mUploadTest.size ();
    }

    public class PetHolder extends RecyclerView.ViewHolder  {

        public TextView textViewType;
        public TextView textViewFamily;
        public TextView textViewDescription;
        public ImageView imageView;
        public TextView mLatReturn;
        public TextView mLongReturn;
        public TextView mUploaderNew;
        OnItemClickListener itemClickListener;


        public PetHolder( @NonNull View itemView ) {
            super ( itemView );


            textViewFamily = itemView.findViewById ( R.id.text_view_family_test_two);

            imageView = itemView.findViewById ( R.id.image_view_upload_test_three );
            mLatReturn=itemView.findViewById ( R.id.tv_latitude_return_three );
            mLongReturn=itemView.findViewById ( R.id.tv_longtitude_return_three);
            mUploaderNew=itemView.findViewById ( R.id.tv_uploader_three );
            textViewType=itemView.findViewById ( R.id.text_view_type_test_three );
            textViewDescription=itemView.findViewById ( R.id.text_view_description_test_three );
            textViewFamily=itemView.findViewById ( R.id.text_view_family_test_three );


            itemView.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick( View view ) {
                    if (mListener != null) {

                        int position = getAdapterPosition ();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick ( position );
                        }


                    }
                }
            } );


        }


    }





}
