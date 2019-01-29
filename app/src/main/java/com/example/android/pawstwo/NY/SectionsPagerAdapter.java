package com.example.android.pawstwo.NY;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionsPagerAdapter extends FragmentPagerAdapter {


    public SectionsPagerAdapter( FragmentManager fm ) {


        super ( fm );

    }

    @Override
    public Fragment getItem( int position ) {

        switch (position){
            case 0:
                    NyRequestsFragment nyRequestsFragment = new NyRequestsFragment ();
            return nyRequestsFragment;

            case 1:
                NyChatsFragment nyChatsFragment=new NyChatsFragment ();
                return nyChatsFragment;

            case 2:
                NyFriendsfragment nyFriendsfragment= new NyFriendsfragment ();
                return  nyFriendsfragment;

                default:
                    return null;

        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){

        switch (position){
            case 0:
                return "REQUESTS";
            case 1:
                return "CHATS";
            case 2:
                return "SEARCH USERS";

            default:
                return null;
        }

    }
}
