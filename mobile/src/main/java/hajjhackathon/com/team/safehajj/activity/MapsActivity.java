package hajjhackathon.com.team.safehajj.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;


import hajjhackathon.com.team.safehajj.R;
import hajjhackathon.com.team.safehajj.connection.gps.DatabaseRepo;
import hajjhackathon.com.team.safehajj.connection.gps.HajjLocation;
import hajjhackathon.com.team.safehajj.connection.gps.IDataBaseRepo;
import hajjhackathon.com.team.safehajj.connection.gps.TrackingService;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,IDataBaseRepo {

    private GoogleMap mMap;
    private static final int PERMISSIONS_REQUEST = 100;
    private boolean fireBaseServiceStarted =  false;
    private List<HajjLocation> allLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Check whether GPS tracking is enabled//
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            finish();
        }
        //Check whether this app has access to the location permission//
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        //If the location permission has been granted, then start the TrackerService//
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {
            //If the app doesn’t currently have access to the user’s location, then request access//

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");

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



        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));ƒ
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        //If the permission has been granted...//
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //...then start the GPS tracking service//
            startTrackerService();
        } else {
            //If the user denies the permission request, then display a toast with some more information//
            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }

    //Start the TrackerService//
    private void startTrackerService() {
        startService(new Intent(this, TrackingService.class));
        //Notify the user that tracking has been enabled//
        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();
        DatabaseRepo.getAllLocations(this);

//        //Close MainActivity//
//        finish();
    }


    @Override
    public void showAllLocations(List<HajjLocation> locations) {
        this.allLocations = locations;
        LatLng latLng = null;
        for(int i =0;i<allLocations.size();i++) {
            HajjLocation location = allLocations.get(i);
            latLng = new LatLng(location.getLatitude()
                    , location.getLongitude());

            mMap.addMarker(new MarkerOptions().position(latLng));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        // Zoom in, animating the camera. 
        mMap.animateCamera(CameraUpdateFactory.zoomIn());

        // TODO: 8/2/18  handle rejection permissions


    }
}
