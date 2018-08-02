package hajjhackathon.com.team.safehajj.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hajjhackathon.com.team.safehajj.R
import hajjhackathon.com.team.safehajj.AppNavigator
import hajjhackathon.com.team.safehajj.fragment.CreateJoinCircleFragment

class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        AppNavigator.loadFragment(this, CreateJoinCircleFragment.newInstance(),getAuthContainerId(),false)
    }

    private fun getAuthContainerId(): Int {
        return R.id.authContainer
    }


}
