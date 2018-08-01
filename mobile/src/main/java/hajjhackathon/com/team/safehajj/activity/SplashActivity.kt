package hajjhackathon.com.team.safehajj.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import hajjhackathon.com.team.R
import hajjhackathon.com.team.safehajj.AppConstants
import hajjhackathon.com.team.safehajj.AppNavigator
import hajjhackathon.com.team.safehajj.util.SharedPreferenceUtil

class SplashActivity : AppCompatActivity() {

    val SPLASH_SCREEN_DELAY_TIME  = 3000L
    var deeplLinkUri : String?  = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({ afterSplashAction()}, SPLASH_SCREEN_DELAY_TIME)
        getDeeplinkIfThere()
    }

    private fun getDeeplinkIfThere() {
        var uri = intent.data;
        if(uri != null) {
            deeplLinkUri = uri.toString()

        }
    }

    private fun afterSplashAction() {
       var userName =  SharedPreferenceUtil.getStringPreference(this, AppConstants.USER_NAME_KEY)
        when (userName != null){
             true -> {
                 AppNavigator.goToMainActivity(this,deeplLinkUri)
             }

            false -> {
                AppNavigator.goToAuthenticationActivity(this,deeplLinkUri)

            }
        }
    }

}