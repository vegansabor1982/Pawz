package com.example.android.pawstwo;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView profileName;
    private TextView profileEmail;
    private Button profileUpdate;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_profile );


        profileEmail = (TextView) findViewById ( R.id.tvProfileEmail );
        profileName = (TextView) findViewById ( R.id.tvProfileName );
        profilePic=(ImageView ) findViewById ( R.id.ivProfilePic );





        firebaseAuth=FirebaseAuth.getInstance ();
        firebaseDatabase=FirebaseDatabase.getInstance ();
        firebaseStorage=FirebaseStorage.getInstance ();


        DatabaseReference databaseReference = firebaseDatabase.getReference().child( "Users").child (firebaseAuth.getUid ());


        StorageReference storageReference = firebaseStorage.getReference ();


        storageReference.child ( firebaseAuth.getUid () ).child ( "Images/Profile Pic" ).getDownloadUrl ().addOnSuccessListener ( new OnSuccessListener<Uri> () {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with (getBaseContext ()).load ( uri ).fit().centerCrop().into (profilePic  );




            }
        } );



        databaseReference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile =dataSnapshot.getValue (UserProfile.class);
                profileName.setText (userProfile.getUserName ()  );
                profileEmail.setText ( userProfile.getUserEmail () );


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText ( ProfileActivity.this, databaseError.getCode (), Toast.LENGTH_LONG ).show ();

            }
        } );
    }
}
