package hajjhackathon.com.team.safehajj.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import hajjhackathon.com.team.safehajj.AppConstants;
import hajjhackathon.com.team.safehajj.AppNavigator;
import hajjhackathon.com.team.safehajj.DistanceCalculator;
import hajjhackathon.com.team.safehajj.R;
import hajjhackathon.com.team.safehajj.connection.gps.DatabaseRepo;
import hajjhackathon.com.team.safehajj.connection.gps.HajjLocation;
import hajjhackathon.com.team.safehajj.connection.gps.IDataBaseRepo;
import hajjhackathon.com.team.safehajj.connection.gps.MyFirebaseMessagingService;
import hajjhackathon.com.team.safehajj.connection.gps.TrackingService;
import hajjhackathon.com.team.safehajj.util.CirclePreference;
import hajjhackathon.com.team.safehajj.util.SharedPreferenceUtil;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, IDataBaseRepo {

    private GoogleMap mMap;
    private static final int PERMISSIONS_REQUEST = 100;
    private boolean fireBaseServiceStarted = false;
    private List<HajjLocation> allLocations;
    private Button logOutButton;
    private boolean isCreateCircle;
    private static final String ISCREATECIRCLE = "isCreateCircle";
    private static final String CIRCLENAME = "circleName";
    private static final String DEEPLINKSCHEMA = "safehajj://circle/";
    private static final String CIRCLENAMEPREF = "circleName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        getExtrasIntent(getIntent().getExtras());
        logOutButton = findViewById(R.id.log_button);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferenceUtil.INSTANCE.remove(MapsActivity.this ,getString(R.string.circle_id_sharedpreferences_key));
                AppNavigator.INSTANCE.goToAuthenticationActivity(MapsActivity.this, null);

            }
        });
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

        ((TextView)findViewById(R.id.circleNameTextView)).setText(CirclePreference.newInstance(this).getCircleName());

    }

    private void getExtrasIntent(Bundle extras) {
        if (extras.containsKey(ISCREATECIRCLE)) {
            isCreateCircle = extras.getBoolean(ISCREATECIRCLE);
            String circleName = null;
            if (extras.containsKey(CIRCLENAME))
                circleName = extras.getString(CIRCLENAME);
            if (isCreateCircle) {
                if (circleName != null) {
                    CirclePreference circlePreference =
                            CirclePreference.newInstance(this);
                    circlePreference.setCircleName(circleName);
                }
                String deepLink = DEEPLINKSCHEMA + TrackingService.getCircleID(false)
                        + "/" + circleName;
                openShareDialog(deepLink);
            }

        }


    }

    private void openShareDialog(String deepLiniUrl) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, deepLiniUrl);
        startActivity(Intent.createChooser(intent, "Share Your Circle"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            getExtrasIntent(getIntent().getExtras());
        }
        TrackingService.circleId = null;
        return true;
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
        DatabaseRepo.getAllLocations("", this);

//        //Close MainActivity//
//        finish();
    }


    @Override
    public void showAllLocations(List<HajjLocation> locations) {
        this.allLocations = locations;
        LatLng currLatLng;
        LatLng adminLatLng = null;
        HajjLocation adminHajjLocation = null;
        List<HajjLocation> oddLocations = new ArrayList<>();
        mMap.clear();
        //region get admin object
        for (int j = 0; j < allLocations.size(); j++) {
            if (allLocations.get(j).isAdmin()) {
                adminHajjLocation = allLocations.get(j);
                break;
            }
        }
        //endregion

        for (HajjLocation hajjLocation : locations) {
            HajjLocation currHajjLocation = hajjLocation;
            currLatLng = new LatLng(currHajjLocation.getLatitude()
                    , currHajjLocation.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(currLatLng));
            if (currHajjLocation.isAdmin()) {
                adminLatLng = currLatLng;
            }
            if (adminHajjLocation != null && !currHajjLocation.isAdmin()) {
                double distanceBetweenPointsAndAdmin = DistanceCalculator.getInstance().greatCircleInMeters(currLatLng,
                        adminLatLng);
                Toast.makeText(this, "distanceBetweenPoints = " + distanceBetweenPointsAndAdmin, Toast.LENGTH_LONG).show();
                //region check for distance
                if (distanceBetweenPointsAndAdmin > 100) {
                    Toast.makeText(this, "distance > 100", Toast.LENGTH_LONG).show();
                    HajjLocation oddHajjLocation = currHajjLocation;
                    oddHajjLocation.setOut(true);
                    if (MyFirebaseMessagingService.isReceived) {
                        oddHajjLocation.setReceived(true);
                    }
                    TrackingService.setUserIsOut(oddHajjLocation);
                    oddLocations.add(oddHajjLocation);

                }
                //endregion

            }
        }

        if (adminLatLng != null) {
            drawSafeCircle(adminLatLng);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(adminLatLng, 12.0f));
            // Zoom in, animating the camera.
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(adminLatLng, 12.0f));
        }
        // TODO: 8/2/18  handle rejection permissions
    }

    private void drawSafeCircle(LatLng centerPointLatLang) {
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(centerPointLatLang)
                .radius(AppConstants.Companion.getCIRCLE_RADUIS())
                .strokeColor(ContextCompat.getColor(this, R.color.circleStrokeColor))
                .fillColor(ContextCompat.getColor(this, R.color.circleColor)));
    }

    private List<HajjLocation> getStaticLocations() {
        List<HajjLocation> locationArrayList = new ArrayList<>();
        HajjLocation location1 = new HajjLocation();
        location1.setLatitude(21.7622922);
        location1.setLongitude(39.1643439);
        location1.setAdmin(true);

        HajjLocation location2 = new HajjLocation();
        location2.setLatitude(21.7537872);
        location2.setLongitude(39.1590548);

        locationArrayList.add(location1);
        locationArrayList.add(location2);

        return locationArrayList;
    }


    private void saveCircleData(String circleName) {

        SharedPreferenceUtil.INSTANCE.setStringPreference(this,"CIRCLENAMEPREF",circleName);

    }


}
