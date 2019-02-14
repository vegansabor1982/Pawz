package com.example.android.pawstwo.NY;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.android.pawstwo.PetAdapterTestTwo;
import com.example.android.pawstwo.R;

public class FavouritesActivity extends AppCompatActivity implements  PetAdapterTestTwo.OnItemClickListener{



    private RecyclerView mRecyclerView;
    private PetAdapterTestTwo mPetAdapterTestTwo;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_favourites );
    }

    @Override
    public void onItemClick( int position ) {




    }

    @Override
    public void onPointerCaptureChanged( boolean hasCapture ) {

    }
}
