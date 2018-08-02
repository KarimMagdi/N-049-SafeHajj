package hajjhackathon.com.team.safehajj.connection.gps;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.UUID;

import hajjhackathon.com.team.safehajj.R;


public class TrackingService extends Service {

    private static final String TAG = TrackingService.class.getSimpleName();
    public static String TOKEN;
    public static String circleId;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildNotification();
        loginToFirebase();
    }

    //Create the persistent notification//
    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.tracking_enabled_notif))
                //Make this notification ongoing so it can’t be dismissed by the user//
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.tracking_enabled);
        startForeground(1, builder.build());
    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Unregister the BroadcastReceiver when the notification is tapped//
            unregisterReceiver(stopReceiver);
            //Stop the Service//
            stopSelf();
        }
    };

    private void loginToFirebase() {
        //Authenticate with Firebase, using the email and password we created earlier//
        String email = getString(R.string.test_email);
        String password = getString(R.string.test_password);
        //Call OnCompleteListener if the user is signed in successfully//
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                //If the user has been authenticated...//
                if (task.isSuccessful()) {
                    //...then call requestLocationUpdates//
                    Log.d(TAG, "Firebase authentication Succedded");
                    requestLocationUpdates();
                } else {
                    //If sign in fails, then log the error//
                    Log.d(TAG, "Firebase authentication failed" + task.getException().getMessage());
                }
            }
        });
    }

    //Initiate the request to track the device's location//
    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        //Specify how often your app should request the device’s location//
        request.setInterval(10000);
        //Get the most accurate location data available//
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //Get Device Token
        TOKEN = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Firebase Token " + TOKEN);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        //If the app currently has access to the location permission...//
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            //...then request location updates//
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    //Get a reference to the database, so your app can perform read and write operations//
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + circleId + "/" + TOKEN + "/location");
                    Location location = (Location) locationResult.getLastLocation();
                    HajjLocation hajjLocation = new HajjLocation();
                    hajjLocation.setAdmin(false);
                    hajjLocation.setAltitude(location.getAltitude());
                    hajjLocation.setLatitude(location.getLatitude());
                    hajjLocation.setLongitude(location.getLongitude());
                    hajjLocation.setTime(location.getTime());
                    hajjLocation.setOut(false);
                    hajjLocation.setToken(TOKEN);
                    //hajjLocation.setVerticalAccuracyMeters(location.getVerticalAccuracyMeters());
                    if (location != null) {
                        //Save the location data to the database//
                        Log.d(TAG, "Firebase Location " + hajjLocation);
                        ref.setValue(hajjLocation);
                        if (true)
                            setUserIsOut(hajjLocation);
                        //Log.d(TAG, "Firebase Locations: " + DatabaseRepo.getAllLocations("",this));
                    }
                }
            }, null);
        }


    }

    public static String getCircleID(boolean isNew) {
        if (isNew) {
            circleId = UUID.randomUUID().toString();
        } else {
            if (circleId == null)
                circleId = UUID.randomUUID().toString();
        }
        return circleId;
    }

    public void setUserIsOut(HajjLocation userIsOut) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("outuser/user");
        ref.setValue(userIsOut);
    }
}
