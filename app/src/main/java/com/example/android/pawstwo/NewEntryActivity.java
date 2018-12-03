package com.example.android.pawstwo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.pawstwo.R.string.Strings;
import static com.example.android.pawstwo.R.string.strings;

public class NewEntryActivity extends AppCompatActivity {


    private DrawerLayout drawer;
    private Button button;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private ProgressDialog progressDialogtwo;
    private ClipData.Item openProfile;
    private Menu drawer_menu;
    Toolbar toolbar;
    private ImageView smallProfilePic;
    private TextView profileEmail;
    private TextView profileName;
    private ListView listView;
    private Spinner spinner;
    private Spinner spinner2;
    private Button maps;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_new_entry );

        maps= findViewById ( R.id.btn_maps );

        maps.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                startActivity ( new Intent ( NewEntryActivity.this, MapsActivity.class ) );

            }
        } );







        /*Spinner spinnerOne =(Spinner)findViewById ( R.id.spinner_type );

        ArrayAdapter<String> myAdapter=new ArrayAdapter<String> (NewEntryActivity.this, android.R.layout.simple_list_item_1,getResources ().getStringArray ( R.array.types )  );
        myAdapter.add ( "Choose Category" );




        myAdapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        spinnerOne.setAdapter ( myAdapter );*/



        spinner = findViewById ( R.id.spinner_type );


        ArrayList<String>categories = new ArrayList<> (  );
        categories.add ( 0,  "Select Category");
        categories.add ( 1, "Found" );
        categories.add ( 2, "Lost" );
        categories.add ( 3, "Gift" );
        categories.add ( 4, "Adopt" );


        ArrayAdapter<String>dataAdapter;
        dataAdapter=new ArrayAdapter ( this ,android.R.layout.simple_spinner_item,categories);
        dataAdapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );

        spinner.setAdapter ( dataAdapter );
//----------------------------------------------------------------------------------------------------------------
        spinner2=findViewById ( R.id.spinner_animal );
        ArrayList<String>animals = new ArrayList<> (  );

        animals.add(0, "Select Family");
        animals.add(1, "Dogs");
        animals.add(2, "Cats");
        animals.add(3, "Other");


        ArrayAdapter<String>dataAdapter2;
        dataAdapter2=new ArrayAdapter ( this ,android.R.layout.simple_spinner_item,animals);
        dataAdapter2.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );

        spinner2.setAdapter ( dataAdapter2 );

//------------------------------------------------------------------------------------------------------------------




    }




}













