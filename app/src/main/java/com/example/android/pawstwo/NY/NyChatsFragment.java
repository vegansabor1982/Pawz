package com.example.android.pawstwo.NY;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.pawstwo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NyChatsFragment extends Fragment {


    public NyChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_ny_chats, container, false );
    }

}
