package com.example.android.pawstwo;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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
    private ImageView petPic;
    private StorageReference storageReference;
    private EditText description;
    private Button saveButton;
    String Type;
    String Family;
    String Description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_new_entry );

        maps = findViewById ( R.id.btn_maps );

        maps.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                startActivity ( new Intent ( NewEntryActivity.this, MapsActivity.class ) );

            }
        } );

        saveButton=findViewById ( R.id.btnSave );

        saveButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                sendPetData ();
                Toast.makeText (NewEntryActivity.this, "Stored Successfully", Toast.LENGTH_SHORT).show ();
                startActivity ( new Intent ( NewEntryActivity.this, HomeActivity.class ) );

            }
        } );











        /*Spinner spinnerOne =(Spinner)findViewById ( R.id.spinner_type );

        ArrayAdapter<String> myAdapter=new ArrayAdapter<String> (NewEntryActivity.this, android.R.layout.simple_list_item_1,getResources ().getStringArray ( R.array.types )  );
        myAdapter.add ( "Choose Category" );




        myAdapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        spinnerOne.setAdapter ( myAdapter );*/


        spinner = findViewById ( R.id.spinner_type );


        ArrayList<String> categories = new ArrayList<> ();
        categories.add ( 0, "Select Category" );
        categories.add ( 1, "Found" );
        categories.add ( 2, "Lost" );
        categories.add ( 3, "Gift" );
        categories.add ( 4, "Adopt" );


        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter ( this, android.R.layout.simple_spinner_item, categories );
        dataAdapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );

        spinner.setAdapter ( dataAdapter );
//----------------------------------------------------------------------------------------------------------------
        spinner2 = findViewById ( R.id.spinner_animal );
        ArrayList<String> animals = new ArrayList<> ();

        animals.add ( 0, "Select Family" );
        animals.add ( 1, "Dogs" );
        animals.add ( 2, "Cats" );
        animals.add ( 3, "Other" );


        ArrayAdapter<String> dataAdapter2;
        dataAdapter2 = new ArrayAdapter ( this, android.R.layout.simple_spinner_item, animals );
        dataAdapter2.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );

        spinner2.setAdapter ( dataAdapter2 );


//------------------------------------------------------------------------------------------------------------------


    }

    private void setupUIViews(){

        spinner= findViewById ( R.id.spinner_type );
        spinner2=findViewById ( R.id.spinner_animal );
        description=findViewById ( R.id.tv_description );
        petPic=findViewById ( R.id.petImage );
        saveButton=findViewById ( R.id.btnSave );
    }

    private Boolean validate() {

        Boolean result = false;

        Type = spinner.getAdapter ().toString ();
        Family = spinner2.getAdapter ().toString ();
        Description =description.getText().toString();
        if (Type.isEmpty () || Family.isEmpty () || Description.isEmpty ()) {

            Toast.makeText ( this, "Details Missing", Toast.LENGTH_SHORT ).show ();
        } else {
            result = true;
        }
        return result;

    }



    private void sendPetData(){

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance ();
       // firebaseAuth = FirebaseAuth.getInstance ();
        DatabaseReference myRefTwo=firebaseDatabase.getReference ();
        PetProfile petProfile= new PetProfile ( Type, Family,Description );myRefTwo.setValue ( petProfile );
    }
}













