package com.example.android.pawstwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PetAdapterTest extends RecyclerView.Adapter<PetAdapterTest.PetHolder> {

    private Context mContext;
    private List <UploadTest> mUploadTest;
    private OnItemClickListener mListener;
    private FirebaseAuth mAuth;

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

        Picasso.get ().load ( uploadCurrent.getmImageUrl () ).fit ().centerCrop ().into ( petHolder.imageView );

    }

    @Override
    public int getItemCount() {
        return mUploadTest.size ();
    }

    public class PetHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener {

        public TextView textViewType;
        public TextView textViewFamily;
        public TextView textViewDescription;
        public ImageView imageView;
        OnItemClickListener itemClickListener;

        public PetHolder(@NonNull View itemView) {
            super ( itemView );

            textViewType=itemView.findViewById ( R.id.text_view_type_test );
            textViewFamily=itemView.findViewById ( R.id.text_view_family_test );
            textViewDescription=itemView.findViewById ( R.id.text_view_description_test );
            imageView=itemView.findViewById ( R.id.image_view_upload_test );


            itemView.setOnClickListener ( this );


            itemView.setOnCreateContextMenuListener ( this );



        }

        @Override
        public void onClick(View view) {

            if (mListener!=null){
                int position = getAdapterPosition ();
                if (position!=RecyclerView.NO_POSITION){
                    mListener.onItemClick ( position );
                }
            }


        }

        @Override
        public void onCreateContextMenu(ContextMenu menu , View view, ContextMenu.ContextMenuInfo contextMenuInfo) {



            menu.setHeaderTitle ("Select Action");
            MenuItem seeSubmission = menu.add ( Menu.NONE,1,1,"See Submission" );
            MenuItem delete = menu.add ( Menu.NONE,2,2,"Delete This" );



            seeSubmission.setOnMenuItemClickListener ( this );
            delete.setOnMenuItemClickListener ( this );

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            if (mListener!=null){
                int position = getAdapterPosition ();
                if (position!=RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId ()){
                        case 1:
                            mListener.onSeeSubmission ( position );
                            return true;
                        case 2: mListener.onDeleteClick ( position );
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick (int position);

        void onSeeSubmission (int position);

        void onDeleteClick (int position);


    }

    public void setOnItemClickListener ( OnItemClickListener listener){

        mListener = listener;




    }

}
