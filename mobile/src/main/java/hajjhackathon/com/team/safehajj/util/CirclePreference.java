package hajjhackathon.com.team.safehajj.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sara on 8/3/2018.
 */

public class CirclePreference {

    private static CirclePreference circlePreference;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String SHAREDNAMECIRCLE = "circleData";
    private static final String CIRCLENAMEPREF = "circleName";
    private static Activity activity;

    private CirclePreference() {

        sharedPreferences = activity.getSharedPreferences(SHAREDNAMECIRCLE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static CirclePreference newInstance(Activity activity1) {
        activity = activity1;
        if (circlePreference == null)
            circlePreference = new CirclePreference();
        return circlePreference;
    }

    public void setCircleName(String circleName) {
        editor.putString(CIRCLENAMEPREF, circleName);
        editor.commit();
    }

    public String getCircleName() {
        return sharedPreferences.getString(CIRCLENAMEPREF, "");
    }

}
