package com.example.android.pawstwo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.android.pawstwo.NY.ChatRoomActivity;
import com.example.android.pawstwo.NY.SearchUsersActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.android.pawstwo.TestHomeActivity.EXTRA_URL;
import static com.example.android.pawstwo.TestHomeActivity.PET_DESCRIPTION;
import static com.example.android.pawstwo.TestHomeActivity.PET_FAMILY;
import static com.example.android.pawstwo.TestHomeActivity.PET_LATITUDE;
import static com.example.android.pawstwo.TestHomeActivity.PET_LONGTITUDE;
import static com.example.android.pawstwo.TestHomeActivity.PET_TYPE;
import static com.example.android.pawstwo.TestHomeActivity.UPLOADER_NAME;

public class SpecificPetProfileActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMapLongClickListener {


    private TextView mSpecType;
    private TextView mSpecFamily;
    private TextView mSpecDescription;
    private TextView mUploadedByUser;
    private CircleImageView mSpecPetPic;
    private Button mSendUserMessage;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private MapView mMapView;

    private static final int Request_User_Location_Code = 99;

    public double latitude;
    public double longtitude;

    private GoogleMap mMapTwo;

    private LatLng mCoords;

    // private GoogleMap mMapTwo;
    private SupportMapFragment supportMapFragment;


    private Glide mGlide;

    Marker marker;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_specific_pet_profile );


        mSpecType = findViewById ( R.id.tv_specific_pet_type );
        mSpecFamily = findViewById ( R.id.tv_specific_pet_family );
        mSpecDescription = findViewById ( R.id.tv_specific_pet_description );
        mUploadedByUser = findViewById ( R.id.tv_specific_pet_uploadedbyuser );
        mSpecPetPic = findViewById ( R.id.iv_specific_pet_pic );




        Intent r = getIntent ();
        String imageUrl = getIntent ().getStringExtra ( EXTRA_URL );
        String petType = getIntent ().getStringExtra ( PET_TYPE );
        String petFamily = getIntent ().getStringExtra ( PET_FAMILY );
        String petDescription = getIntent ().getStringExtra ( PET_DESCRIPTION );
        final String uploaderName = getIntent ().getStringExtra ( UPLOADER_NAME );









        Picasso.with ( getBaseContext () ).load ( imageUrl ).fit ().centerCrop ().into ( mSpecPetPic );
        mSpecType.setText ( petType );
        mSpecFamily.setText ( petFamily );
        mSpecDescription.setText ( petDescription );
        mUploadedByUser.setText ( "Uploaded by: " + uploaderName );


        //-----------------------------------------------------test--------------------------------------------

      /*  SupportMapFragment supportMapFragment = ( SupportMapFragment ) getSupportFragmentManager ()
                .findFragmentById ( R.id.map_specific );

        supportMapFragment.getMapAsync ( this );




        String petLat = getIntent ().getStringExtra ( PET_LATITUDE );
        String petLong = getIntent ().getStringExtra ( PET_LONGTITUDE );

        double latitude = Double.parseDouble ( petLat );
        double longitude = Double.parseDouble ( petLong );


        LatLng latlng = new LatLng ( latitude, longitude );





        marker=mMapTwo.addMarker ( new MarkerOptions ().position ( latlng ).title ( "Marker" ).draggable ( true ) );

        mMapTwo.moveCamera ( CameraUpdateFactory.newLatLng ( latlng ) );
        mMapTwo.animateCamera ( CameraUpdateFactory.zoomTo ( 6 ) );


*/












        //----------------------------------------------test=---------------------------------------------

        mDatabase = FirebaseDatabase.getInstance ();
        mAuth = FirebaseAuth.getInstance ();


        DatabaseReference mRef = mDatabase.getReference ().child ( "Users" ).child ( mAuth.getUid () );


        mSendUserMessage = findViewById ( R.id.btn_send_message );


        mSendUserMessage.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View view ) {


                Intent u = new Intent ( SpecificPetProfileActivity.this, SearchUsersActivity.class );
                startActivity ( u );


            }
        } );


    }


    @Override
    public void onConnected( @Nullable Bundle bundle ) {

        locationRequest = new LocationRequest ();
        locationRequest.setInterval ( 1100 );
        locationRequest.setFastestInterval ( 1100 );
        locationRequest.setPriority ( LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY );

        if (ContextCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates ( googleApiClient, locationRequest, this );


        }

    }

    @Override
    public void onConnectionSuspended( int i ) {

    }

    @Override
    public void onConnectionFailed( @NonNull ConnectionResult connectionResult ) {

    }

    @Override
    public void onLocationChanged( Location location ) {

    }

    @Override
    public void onMapLongClick( LatLng latLng ) {

    }

    @Override
    public void onMapReady( GoogleMap googleMap ) {

        mMapTwo = googleMap;

        if (ContextCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


        }




    }

    protected synchronized void buildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder ( this ).addConnectionCallbacks ( this ).addOnConnectionFailedListener ( this ).addApi ( LocationServices.API ).build ();

        googleApiClient.connect ();


    }

    @Override
    public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ) {


        buildGoogleApiClient ();


    }

    public boolean checkedUserLocationPermission() {

        if (ContextCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale ( this, Manifest.permission.ACCESS_FINE_LOCATION )) {

                ActivityCompat.requestPermissions ( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code );
            } else {
                ActivityCompat.requestPermissions ( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code );
            }
            return false;
        } else {
            return true;
        }


    }


}
