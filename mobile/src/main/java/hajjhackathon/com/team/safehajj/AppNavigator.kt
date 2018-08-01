/*
 * Copyright (c) 2017.  All Code CopyRights reserved For Zeinab Mohamed Abdelmawla
 */

package hajjhackathon.com.team.safehajj

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import hajjhackathon.com.team.safehajj.activity.AuthenticationActivity
import hajjhackathon.com.team.safehajj.activity.MapsActivity


object AppNavigator{

     val DEEP_LINK_URI: String = "deeplink_uri"


    fun goToAuthenticationActivity(activity: Activity, deepLinkUri: String?) {
        val intent = Intent(activity, AuthenticationActivity::class.java)
        deepLinkUri?.let {
            intent.putExtra(DEEP_LINK_URI,it)
        }
        activity.startActivity(intent)
        activity.finish()    }

    /**
     * Navigate to main activity application entry point after splash
     */
    fun goToMapsActivity(activity: Activity , deepLinkUri : String? =  null) {
        val intent = Intent(activity, MapsActivity::class.java)
        deepLinkUri?.let {
            intent.putExtra(DEEP_LINK_URI,it)
        }
        activity.startActivity(intent)
        activity.finish()
    }


    fun loadFragment(activity: FragmentActivity,
                     fragment: Fragment, containerId: Int, isStacked: Boolean) {

        if (!isStacked) {
            activity.supportFragmentManager
                    .beginTransaction().replace(containerId,
                            fragment).commit()
        } else {
            activity.supportFragmentManager.beginTransaction().replace(containerId,
                    fragment,fragment::class.java.name).addToBackStack(null).commit()
        }
    }


}