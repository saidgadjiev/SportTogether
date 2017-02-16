package ru.mail.sporttogether.auth.core.social_networks

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import ru.mail.sporttogether.auth.core.ISocialNetwork
import ru.mail.sporttogether.auth.core.SocialPerson
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnRequestSocialPersonCompleteListener


/**
 * Created by said on 17.11.16
 */

class GoogleSocialNetwork : ISocialNetwork, GoogleApiClient.OnConnectionFailedListener {

    override val id: Int
        get() = 10

    override val token: String
        get() = ""

    override fun tryAutoLogin(onLoginCompleteListener: OnLoginCompleteListener): Boolean {
        return false
    }

    override fun getLoadedSocialPerson(): SocialPerson? {
        return null
    }

    override fun setOnLoginCompleteListener(onLoginCompleteListener: OnLoginCompleteListener) {

    }

    override fun logout() {

    }

    override fun login(activity: AppCompatActivity, onLoginCompleteListener: OnLoginCompleteListener) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        val mGoogleApiClient = GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onDestroy() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    override val isConnected: Boolean
        get() = false


    override fun requestPerson(onRequestSocialPersonCompleteListener: OnRequestSocialPersonCompleteListener) {

    }

    companion object {
        @JvmStatic val RC_SIGN_IN = 1824
        @JvmStatic val ID = 3
    }
}
