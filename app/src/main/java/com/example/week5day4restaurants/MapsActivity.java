package com.example.week5day4restaurants;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, PermissionsManager.IPermissionManager
        , GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static long UPDATE_INTERVAL = 3000;
    private static long FASTEST_INTERVAL = 5000;
    private String GEOFENCE_REQ_ID = "100";
    private float RADIUS = 100;
    LocationRequest locationRequest;
    List<Geofence> geofenceList;
    private GeofencingClient mGeofencingClient;

    private GoogleMap mMap;
    PermissionsManager permissionsManager;
    Location currentDisplayLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        permissionsManager = new PermissionsManager(this);
        permissionsManager.checkPermission();
        mGeofencingClient = LocationServices.getGeofencingClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

//    private PendingIntent getGeofencePendingIntent() {
//        // Reuse the PendingIntent if we already have it.
//        if (mGeofencePendingIntent != null) {
//            return mGeofencePendingIntent;
//        }
//        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
//        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
//        // calling addGeofences() and removeGeofences().
//        mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
//                FLAG_UPDATE_CURRENT);
//        return mGeofencePendingIntent;
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.checkResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPerissionResult(boolean isGranted) {
        if(isGranted){
            //getLastKnownLocation();
            Log.d("TAG", "onPerissionResult: Successful");
            //getCurrentLocation();

        }

    }


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

        // Add a marker in Sydney and move the camera

        getCurrentLatLngFromAddress("2215 D and B Dr SE Marietta GA", "dandb");
        getCurrentLatLngFromAddress("2830 Windy Hill Rd SE, Marietta, GA 30067", "pappadeaux");
        getCurrentLatLngFromAddress("6700 Powers Ferry Rd NW, Sandy Springs, GA 30339", "raysonriver");
        getCurrentLatLngFromAddress("2014 Powers Ferry Rd #450, Atlanta, GA 30339", "piubello");
        getCurrentLatLngFromAddress("2788 Windy Hill Rd SE, Marietta, GA 30067", "pappasitos");
//        GeofencingRequest request = new GeofencingRequest.Builder()
//                // Notification to trigger when the Geofence is created
//                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
//                .addGeofences(geofenceList) // add a Geofence
//                .build();
//        PendingResult<Status> addGeofences ();
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(mobileAppsCompany));
        mMap.setMinZoomPreference(10);

    }


    @Override
    public void onLocationChanged(Location location) {
        //Log.d("TAG", "onLocationChanged: Location CHANGED");
//        moveToNewLocation(location, "YOU ARE HERE");
//        getCurrentLocationAddress(new LatLng(location.getLatitude(), location.getLongitude()));
        //getCurrentLatLngFromAddress("2215 D and B Dr SE Marietta GA");
        //2215 D and B Dr SE, Marietta, GA 30067
        //http://mbastl.com/wp-content/uploads/2018/05/Dave-and-Busters-logo-Copy.png
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressLint("MissingPermission")
    private void getLastKnownLocation(){
        FusedLocationProviderClient fusedLocationProviderClient = getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    //moveToNewLocation(location, "Current Phone Location");
                    currentDisplayLocation = location;
                    Log.d("TAG", "onSuccess: " + location.toString());
                }else {
                    Log.d("TAG", "onFailure: Location was Null!!");
                }
            }
        });
    }

    private  void moveToNewLocation(Location location, String locationName){
        if(location != null){
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Mobile Apps"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.setMinZoomPreference(10);

        }
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation(){
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setInterval(UPDATE_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                onLocationChanged(locationResult.getLastLocation());
            }
        },Looper.myLooper());
    }

    public void getCurrentLocationAddress(LatLng latlng){
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
            for(Address address : addresses){
                Log.d("TAG", "GetCurrentLocationAddress: " + address.getAddressLine(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getCurrentLatLngFromAddress(String address, String iconName){
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresseWithLatLng = geocoder.getFromLocationName(address, 1);
            LatLng newlatLng = new LatLng(addresseWithLatLng.get(0).getLatitude(), addresseWithLatLng.get(0).getLongitude());
            mMap.addMarker(new MarkerOptions().position(newlatLng).title(addresseWithLatLng.get(0).getAddressLine(0))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(iconName, 50, 50))));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newlatLng));
//            Geofence geofence = new Geofence.Builder()
//                    .setRequestId(GEOFENCE_REQ_ID) // Geofence ID
//                    .setCircularRegion( addresseWithLatLng.get(0).getLatitude(), addresseWithLatLng.get(0).getLongitude(), RADIUS) // defining fence region
//                    .setExpirationDuration( 10000000 ) // expiring date
//                    // Transition types that it should look for
//                    .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT )
//                    .build();
//            geofenceList.add(geofence);
            Log.d("TAG", "getCurrentLatLngFromAddress: " + newlatLng.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view){
        moveToNewLocation(currentDisplayLocation, "YOU ARE HERE");
    }

    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
}
