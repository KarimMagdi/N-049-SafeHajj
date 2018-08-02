package hajjhackathon.com.team.safehajj.connection.gps;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseRepo {
    private static DatabaseReference mDatabase;
    private static final String TAG = DatabaseRepo.class.getSimpleName();
    static List<HajjLocation> locationList = new ArrayList<>();


    private static void getInstance() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
    }

    public static void getAllLocations(String circleId , final IDataBaseRepo iDataBaseRepo) {
        getInstance();
        mDatabase.child(TrackingService.circleId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Object> objectMap = (HashMap<String, Object>)
                        dataSnapshot.getValue();
                if (objectMap == null)
                    return;
                locationList.clear();
                for (Object obj : objectMap.values()) {
                    if (obj instanceof Map) {
                        Map<String, Object> mapObj = (Map<String, Object>) obj;
                        HajjLocation hajjLocation = new HajjLocation();
                        hajjLocation.setAdmin((boolean) mapObj.get(ModelConstants.ADMIN));
                        //hajjLocation.setLongitude((double) mapObj.get(ModelConstants.ALTITUDE));
                        hajjLocation.setLatitude((double) mapObj.get(ModelConstants.LATITUDE));
                        hajjLocation.setLongitude((double) mapObj.get(ModelConstants.LONGITUDE));
                        hajjLocation.setTime((long) mapObj.get(ModelConstants.TIME));
//                        hajjLocation.setVerticalAccuracyMeters((double) mapObj.get(ModelConstants.VERTICALACCURACYMETERS));
                        locationList.add(hajjLocation);
                    }
                }
                Log.d(TAG, "Locations:" + locationList.size() + "");
                iDataBaseRepo.showAllLocations(locationList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}
