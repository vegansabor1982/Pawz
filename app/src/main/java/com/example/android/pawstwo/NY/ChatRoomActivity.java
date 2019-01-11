package com.example.android.pawstwo.NY;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.pawstwo.MainActivity;
import com.example.android.pawstwo.R;
import com.example.android.pawstwo.TestHomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChatRoomActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSelecttionsPageAdapter;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_chat_room );

        mToolbar= (Toolbar) findViewById ( R.id.ny_main_page_toolbar );
        setSupportActionBar (mToolbar  );
        getSupportActionBar ().setTitle ( "Chat" );
        mViewPager=findViewById ( R.id.ny_tabPager );

        mAuth=FirebaseAuth.getInstance ();
    }

    @Override
    protected void onStart() {
        super.onStart ();
        FirebaseUser currentuser= mAuth.getCurrentUser ();

        if (currentuser==null){
            Intent mainActivity = new Intent ( ChatRoomActivity.this, MainActivity.class );
            startActivity ( mainActivity );
            finish ();
        }

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        super.onCreateOptionsMenu ( menu );

        getMenuInflater ().inflate ( R.menu.ny_menu,menu );
        return true;
    }

    private void Logout() {

        mAuth= FirebaseAuth.getInstance ();
        mAuth.signOut ();
        finish ();
        Intent i = new Intent ( ChatRoomActivity.this, MainActivity.class  );
        startActivity ( i );
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId()){
            case R.id.ny_main_logOut_btn:{
                Logout ();
            }



            break;
        }

        return super.onOptionsItemSelected(item);
    }

}
