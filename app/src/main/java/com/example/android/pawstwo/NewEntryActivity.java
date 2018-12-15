package com.example.android.pawstwo;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class NewEntryActivity extends AppCompatActivity {




    private DrawerLayout drawer;
    private Button button;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
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
    private EditText description;
    private Button saveButton;
    String Type;
    String Family;
    String Description;
    private DatabaseReference mDatabase;
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE=123;
    Uri imagePath2;
    private StorageReference storageReference;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PICK_IMAGE && resultCode==RESULT_OK && data.getData ()!=null){

            imagePath2=data.getData ();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver () ,imagePath2 );
                petPic.setImageBitmap ( bitmap );
            } catch (IOException e) {
                e.printStackTrace ();
            }

        }
        super.onActivityResult ( requestCode, resultCode, data );
    }



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


        setupUIViews ();

        firebaseAuth = FirebaseAuth.getInstance ();
        firebaseStorage= FirebaseStorage.getInstance ();

        storageReference= firebaseStorage.getReference ();
        petPic.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent glry = new Intent();
                glry.setType ( "image/*" );
                glry.setAction (Intent.ACTION_GET_CONTENT  );
                startActivityForResult ( Intent.createChooser ( glry, "Select Image" ), PICK_IMAGE );

            }
        } );


        saveButton=findViewById ( R.id.btnSave );

        saveButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                spinner= findViewById ( R.id.spinner_type );
                spinner2=findViewById ( R.id.spinner_animal );
                description= findViewById ( R.id. tv_description );


                if (validate ()){

                    String Description = description.getText ().toString ().trim ();
                    String Family= spinner.getSelectedItem ().toString ().trim ();
                    String Type= spinner2.getSelectedItem ().toString ().trim ();
                }

                sendPetData ();


                Toast.makeText (NewEntryActivity.this, "Stored Successfully! Now save the Pet Location Below.", Toast.LENGTH_SHORT).show ();
                //startActivity ( new Intent ( NewEntryActivity.this, HomeActivity.class ) );

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

        Type = spinner.getSelectedItem ().toString ();
        Family = spinner2.getSelectedItem ().toString ();
        Description =description.getText().toString();
        if (Type.isEmpty () || Family.isEmpty () || Description.isEmpty ()|| imagePath2==null) {

            Toast.makeText ( this, "Details Missing", Toast.LENGTH_SHORT ).show ();
        } else {
            result = true;
        }
        return result;

    }




    private void sendPetData() {

        StorageReference imageReference = storageReference.child ( firebaseAuth.getUid ()).child ( "Pet Images/"+UUID.randomUUID () );
        UploadTask uploadTask = imageReference.putFile ( imagePath2 );

        uploadTask.addOnFailureListener ( new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText ( NewEntryActivity.this, "Upload failed", Toast.LENGTH_LONG ).show ();

            }
        } );
        uploadTask.addOnSuccessListener ( new OnSuccessListener<UploadTask.TaskSnapshot> () {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText ( NewEntryActivity.this, "Upload Successful!", Toast.LENGTH_LONG ).show ();

            }
        } );

        mDatabase = FirebaseDatabase.getInstance ().getReference ("Pets");
        //PetProfile petProfile = null;



       PetProfile petProfile = new PetProfile ( Description, Family, Type );
      // mDatabase.child ( "Pets" ).child ( "Pet2" ).setValue ( petProfile );

        mDatabase.push ().setValue ( petProfile );


        // FirebaseDatabase firebaseDatabase1=FirebaseDatabase.getInstance ();
        // firebaseDatabase1.child
        // firebaseAuth = FirebaseAuth.getInstance ();
        // DatabaseReference myRefTwo=firebaseDatabase1.getReference ();

        //myRefTwo.setValue ( petProfile );
    }
}