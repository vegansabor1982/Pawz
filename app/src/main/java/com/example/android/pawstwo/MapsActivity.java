package com.example.android.pawstwo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.firebase.geofire.GeoFire;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMapLongClickListener {


    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    // private double lastLocation;
    private Place lastPlace;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private Geocoder geocoder;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds ( new LatLng ( -40, -168 ), new LatLng ( 71, 136 ) );
    Place locationAddress;
    private String latLng;
    Marker marker;
    private PlaceAutocompleteFragment placeAutocompleteFragment;
    ZoomControls zoomControls;
    private FirebaseAuth firebaseAuth;
    private Button locButton;
    private Button saveButton;
    private Location locationMap;
    String Latitude;
    String Longtitude;
    private double latitude;
    private double longtitude;




    DatabaseReference ref;
    DatabaseReference ref2;
    private GeoFire geoFire;

    private Location placeLocation;
   private LatLng petLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_maps );






         placeAutocompleteFragment = ( PlaceAutocompleteFragment ) getFragmentManager ().findFragmentById ( R.id.place_autocomplete_fragment );



        placeAutocompleteFragment.setOnPlaceSelectedListener ( new PlaceSelectionListener () {
            @Override
            public void onPlaceSelected(final Place place) {
                final LatLng latLngloc = place.getLatLng ();

                if (marker != null) {
                    marker.remove ();
                }




















               marker = mMap.addMarker ( new MarkerOptions ().position ( latLngloc ).title ( "Save Pet Location").draggable ( true ) );









                mMap.moveCamera ( CameraUpdateFactory.newLatLng ( latLngloc ) );
                mMap.animateCamera ( CameraUpdateFactory.zoomTo ( 6 ) );


                locButton=findViewById ( R.id.btn_save_location );

                locButton.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {

                      // String Latitude=Double.toString ( latitude );
                      // String Longtitude=Double.toString ( longtitude );


                        DatabaseReference petProfile = FirebaseDatabase.getInstance ().getReference("Pet Position" );
                        FirebaseMarker marker1 = new FirebaseMarker (  );
                        petProfile.push ().setValue ( marker.getPosition () );

                        Toast.makeText ( MapsActivity.this,"Thank you for your Submission", Toast.LENGTH_SHORT ).show ();

                        Intent i = new Intent ( MapsActivity.this, HomeActivity.class );
                        startActivity ( i );

                        mMap.setOnMarkerDragListener ( new GoogleMap.OnMarkerDragListener () {
                            @Override
                            public void onMarkerDragStart(Marker marker) {


                            }

                            @Override
                            public void onMarkerDrag(Marker marker) {

                            }

                            @Override
                            public void onMarkerDragEnd(final Marker marker) {
                                locButton.setOnClickListener ( new View.OnClickListener () {
                                    @Override
                                    public void onClick(View view) {
                                        DatabaseReference petProfile = FirebaseDatabase.getInstance ().getReference ("Pet Position");
                                        FirebaseMarker marker1 = new FirebaseMarker (  );
                                        petProfile.push ().setValue ( marker.getPosition () );

                                        Toast.makeText ( MapsActivity.this,"Thank you for your Submission", Toast.LENGTH_SHORT ).show ();

                                        Intent k = new Intent ( MapsActivity.this, HomeActivity.class );
                                        startActivity ( k );

                                    }
                                } );


                            }
                        } );
                    }
                } );

















                placeAutocompleteFragment.getView ().findViewById ( R.id.place_autocomplete_clear_button ).setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {

                        mMap.clear ();
                    }
                } );




            }

            @Override
            public void onError(Status status) {

            }
        } );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkedUserLocationPermission ();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = ( SupportMapFragment ) getSupportFragmentManager ()
                .findFragmentById ( R.id.map );
        mapFragment.getMapAsync ( this );

        zoomControls = ( ZoomControls ) findViewById ( R.id.zcZoom );
        zoomControls.setOnZoomOutClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                mMap.animateCamera ( CameraUpdateFactory.zoomOut () );

            }
        } );
        zoomControls.setOnZoomInClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                mMap.animateCamera ( CameraUpdateFactory.zoomIn () );

            }
        } );















    }
  /* public void onSearch (View view){



     //  mSearchText= findViewById ( R.id.tv_Search );
       PlaceAutocompleteFragment edtAdress = (PlaceAutocompleteFragment )getFragmentManager ().findFragmentById ( R.id.place_autocomplete_fragment ) ;
      // edtAdress.getView ().findViewById ( R.id. btn_search ).setVisibility ( View.GONE );
       edtAdress.setOnPlaceSelectedListener ( new PlaceSelectionListener () {
           @Override
           public void onPlaceSelected(Place place) {
               locationAddress=place;

           }

           @Override
           public void onError(Status status) {
               Log.e("ERROR", status.getStatusMessage ());

           }
       } );




       // AutoCompleteTextView location_tf = (AutoCompleteTextView ) findViewById ( R.id.tv_Search );
        String location = locationAddress.getId().toString();

        String.format ( String.valueOf ( locationAddress.getLatLng ().latitude ),locationAddress.getLatLng ().longitude);
        List<Address> addressList=null;





        if (location!=null || !location.equals ("")){

            Geocoder geocoder = new Geocoder ( this );
            try {
               addressList= geocoder.getFromLocationName ( location, 1 );
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }Address locationAddress = addressList.get ( 0 );
        LatLng latLng=new LatLng ( locationAddress.getLatitude (), locationAddress.getLongitude () );
        mMap.addMarker ( new MarkerOptions ().position ( latLng ).title ( "Marker" ) );
        mMap.animateCamera ( CameraUpdateFactory.newLatLng ( latLng ) );




    }
    public void onError(Status status){

        Toast.makeText ( MapsActivity.this, " "+status.toString (),Toast.LENGTH_SHORT ).show ();

    }*/


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            buildGoogleApiClient ();


        }


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


    protected synchronized void buildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder ( this ).addConnectionCallbacks ( this ).addOnConnectionFailedListener ( this ).addApi ( LocationServices.API ).build ();

        googleApiClient.connect ();


    }

    /*@Override
    public void onLocationChanged(Location locationAdress) {
        lastLocation=  locationAddress;

        if (currentUserLocationMarker!=null){

            currentUserLocationMarker.remove ();
        }

        LatLng latLng = new LatLng ( locationAddress.getLatitude (),locationAddress.getLongitude () );

        MarkerOptions markerOptions = new MarkerOptions ();
        markerOptions.position ( latLng );
        markerOptions.title ( "User Current Location" );
        markerOptions.icon ( BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED) );

        currentUserLocationMarker= mMap.addMarker ( markerOptions );

        mMap.moveCamera ( CameraUpdateFactory.newLatLng ( latLng ) );
        mMap.animateCamera ( CameraUpdateFactory.zoomBy ( 14 ) );

        if (googleApiClient!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates ( googleApiClient, this );

        }*/


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest ();
        locationRequest.setInterval ( 1100 );
        locationRequest.setFastestInterval ( 1100 );
        locationRequest.setPriority ( LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY );

        if (ContextCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates ( googleApiClient, locationRequest, this );


        }



   /* @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }*/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient ();
                        }
                        mMap.setMyLocationEnabled ( true );

                    }
                } else {
                    Toast.makeText ( this, "Permission Denied...", Toast.LENGTH_SHORT ).show ();
                }
                return;


        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    @Override
    public void onLocationChanged(Location location) {



     /* lastLocation=location;
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        String petId=FirebaseAuth.getInstance ().getCurrentUser ().getUid ();
        ref=FirebaseDatabase.getInstance ().getReference ("Pet Location");

       GeoFire geoFire =new GeoFire ( ref );
        geoFire.setLocation ( petId, new GeoLocation ( lastLocation.getLatitude (), lastLocation.getLongitude () ), new GeoFire.CompletionListener ()

        {
            @Override
            public void onComplete(String key, DatabaseError error) {


            }
        } );*/








   }


    @Override
    public void onMapLongClick(LatLng latLng) {



    }





}


//finally working!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
