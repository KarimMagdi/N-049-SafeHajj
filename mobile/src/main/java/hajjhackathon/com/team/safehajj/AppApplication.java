package hajjhackathon.com.team.safehajj;

import android.app.Application;

public class AppApplication extends Application {

    private static AppApplication context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
    }

    public static AppApplication getContext() {
        return context;
    }
}
