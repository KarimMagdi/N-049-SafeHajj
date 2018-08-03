package hajjhackathon.com.team.safehajj;

import com.google.android.gms.maps.model.LatLng;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;


/**
 * Created by Esraa on 8/2/2018.
 */

public class DistanceCalculator {
    private static double PI_RAD = Math.PI / 180.0;
    private static DistanceCalculator distanceCalculator;


    public static DistanceCalculator getInstance() {
        if (distanceCalculator == null)
            return new DistanceCalculator();
        return distanceCalculator;
    }

    public double greatCircleInFeet(LatLng latLng1, LatLng latLng2) {
        return greatCircleInKilometers(latLng1.latitude, latLng1.longitude, latLng2.latitude,
                latLng2.longitude) * 3280.84;
    }

    public double greatCircleInMeters(LatLng latLng1, LatLng latLng2) {
        if (latLng1 == null || latLng2 == null)
            return 0;
        return greatCircleInKilometers(latLng1.latitude, latLng1.longitude, latLng2.latitude,
                latLng2.longitude) * 1000;
    }

    public double greatCircleInKilometers(double lat1, double long1, double lat2, double long2) {
        double phi1 = lat1 * PI_RAD;
        double phi2 = lat2 * PI_RAD;
        double lam1 = long1 * PI_RAD;
        double lam2 = long2 * PI_RAD;

        return 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1));
    }
}
