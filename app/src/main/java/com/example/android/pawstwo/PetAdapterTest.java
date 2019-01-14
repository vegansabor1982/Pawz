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
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PetAdapterTest extends RecyclerView.Adapter<PetAdapterTest.PetHolder> {

    private Context mContext;
    private List <UploadTest> mUploadTest;
    private OnItemClickListener mListener;
    private FirebaseAuth mAuth;

    public interface OnItemClickListener {

        void onItemClick(int position);


    }

   public void setOnItemClickListener( OnItemClickListener listener ){

        mListener=listener;
   }

    public PetAdapterTest (Context context,List<UploadTest> uploads){

        mContext=context;
        mUploadTest=uploads;
    }

    @NonNull
    @Override
    public PetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from (mContext).inflate ( R.layout.image_item_test,parent, false );
        return new PetHolder ( v );
    }

    @Override
    public void onBindViewHolder( PetHolder petHolder, int position) {

        UploadTest uploadCurrent = mUploadTest.get ( position );
        petHolder.textViewType.setText ( uploadCurrent.getmType () );
        petHolder.textViewFamily.setText ( uploadCurrent.getmFamily () );
        petHolder.textViewDescription.setText ( uploadCurrent.getmDescription () );
        petHolder.mLatReturn.setText ( uploadCurrent.getmLat ());
        petHolder.mLongReturn.setText ( uploadCurrent.getmLongt () );
        petHolder.mUploaderNew.setText ( uploadCurrent.getmUploaderName () );

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

            textViewType = itemView.findViewById ( R.id.text_view_type_test );
            textViewFamily = itemView.findViewById ( R.id.text_view_family_test );
            textViewDescription = itemView.findViewById ( R.id.text_view_description_test );
            imageView = itemView.findViewById ( R.id.image_view_upload_test );
            mLatReturn=itemView.findViewById ( R.id.tv_latitude_return );
            mLongReturn=itemView.findViewById ( R.id.tv_longtitude_return);
            mUploaderNew=itemView.findViewById ( R.id.tv_uploader );


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
