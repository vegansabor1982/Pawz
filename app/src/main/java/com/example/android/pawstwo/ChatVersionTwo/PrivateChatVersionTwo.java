package com.example.android.pawstwo.ChatVersionTwo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.pawstwo.NY.SearchUsersActivity;
import com.example.android.pawstwo.R;

import java.util.ArrayList;

public class PrivateChatVersionTwo extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Button mSearchBtn;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_private_chat_version_two );


        mTabLayout=findViewById ( R.id.tab_layout );
        mViewPager=findViewById ( R.id. view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter ( getSupportFragmentManager () );

        viewPagerAdapter.addFragment ( new ChatsFragment (), "Chats" );
        viewPagerAdapter.addFragment ( new UsersFragment (), "Users" );

        mViewPager.setAdapter ( viewPagerAdapter );

        mTabLayout.setupWithViewPager ( mViewPager );






    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment > fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter ( FragmentManager fm ){

            super(fm);
            this.fragments=new ArrayList<> (  );
            this.titles=new ArrayList<> (  );
        }


        @Override
        public Fragment getItem( int position ) {
            return fragments.get ( position );
        }

        @Override
        public int getCount() {
            return fragments.size ();
        }

        public void addFragment (Fragment fragment, String title){

            fragments.add ( fragment );
            titles.add ( title );

        }

        @Nullable
        @Override
        public CharSequence getPageTitle( int position ) {
            return titles.get ( position );
        }
    }



}
