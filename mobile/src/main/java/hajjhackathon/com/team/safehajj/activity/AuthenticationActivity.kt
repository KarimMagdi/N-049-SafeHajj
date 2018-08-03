package hajjhackathon.com.team.safehajj.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hajjhackathon.com.team.safehajj.AppNavigator
import hajjhackathon.com.team.safehajj.R
import hajjhackathon.com.team.safehajj.fragment.CreateJoinCircleFragment
import hajjhackathon.com.team.safehajj.fragment.RegisterProfileFragment

class AuthenticationActivity : AppCompatActivity() {

    val DEEP_LINK_URI: String = "deeplink_uri"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        if (intent.extras != null && intent.extras.containsKey(DEEP_LINK_URI)) {
            var deepLink = intent.extras.getString(DEEP_LINK_URI);
            var registerFragment = RegisterProfileFragment.newInstance(false)
            registerFragment.setDeepLink(deepLink);
            AppNavigator.loadFragment(this, registerFragment, getAuthContainerId(), false)
        } else {
            AppNavigator.loadFragment(this, CreateJoinCircleFragment.newInstance(), getAuthContainerId(), false)
        }

    }


    private fun getAuthContainerId(): Int {
        return R.id.authContainer
    }


}
