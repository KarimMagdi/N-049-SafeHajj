package hajjhackathon.com.team.safehajj.connection.gps;

import java.io.Serializable;

public class HajjLocation implements Serializable {

    private boolean isAdmin;
    private double altitude;
    private double latitude;
    private double longitude;
    private long time;
    private double verticalAccuracyMeters;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getVerticalAccuracyMeters() {
        return verticalAccuracyMeters;
    }

    public void setVerticalAccuracyMeters(double verticalAccuracyMeters) {
        this.verticalAccuracyMeters = verticalAccuracyMeters;
    }
}
