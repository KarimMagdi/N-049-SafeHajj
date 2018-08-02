package hajjhackathon.com.team.safehajj.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sara on 8/3/2018.
 */

public class CirclePreference {

    private static CirclePreference circlePreference;
    private static SharedPreferenceUtil sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String SHAREDNAMECIRCLE = "circleData";
    private static final String CIRCLENAMEPREF = "circleName";
    private static Activity activity;

    private CirclePreference() {

        sharedPreferences = SharedPreferenceUtil.INSTANCE;
    }

    public static CirclePreference newInstance(Activity activity1) {
        activity = activity1;
        if (circlePreference == null)
            circlePreference = new CirclePreference();
        return circlePreference;
    }

    public void setCircleName(String circleName) {
        sharedPreferences.setStringPreference(activity,CIRCLENAMEPREF,circleName);
    }

    public String getCircleName() {
        return (sharedPreferences.getStringPreference(activity,CIRCLENAMEPREF) != null) ?
                sharedPreferences.getStringPreference(activity,CIRCLENAMEPREF):"";
    }

}
