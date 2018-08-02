package hajjhackathon.com.team.safehajj.activity

import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import hajjhackathon.com.team.safehajj.AppConstants
import hajjhackathon.com.team.safehajj.AppNavigator
import hajjhackathon.com.team.safehajj.R
import hajjhackathon.com.team.safehajj.connection.gps.TrackingService
import hajjhackathon.com.team.safehajj.util.SharedPreferenceUtil

class SplashActivity : AppCompatActivity() {

    val SPLASH_SCREEN_DELAY_TIME = 2000L
    var deeplLinkUri: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({ afterSplashAction() }, SPLASH_SCREEN_DELAY_TIME)
        getDeepLinkIfThere()
    }

    private fun getDeepLinkIfThere() {
        var uri = intent.data
        if (uri != null) {
            deeplLinkUri = uri.toString()
            AppNavigator.goToAuthenticationActivity(this, deeplLinkUri)
        }
    }

    private fun afterSplashAction() {
        var userName = SharedPreferenceUtil.getStringPreference(this, AppConstants.USER_NAME_KEY)
        var currentCircleId = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.circle_id_sharedpreferences_key),
                TrackingService.circleId)
        when (currentCircleId != null) {

            true -> {
                TrackingService.circleId = currentCircleId
                AppNavigator.goToMapsActivity(this, deeplLinkUri, false,"")
            }
            false -> {
                AppNavigator.goToAuthenticationActivity(this, deeplLinkUri)

            }
        }
//        AppNavigator.goToMapsActivity(this,deeplLinkUri)
    }

}