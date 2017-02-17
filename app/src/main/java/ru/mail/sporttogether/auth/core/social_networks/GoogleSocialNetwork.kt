package ru.mail.sporttogether.auth.core.social_networks

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import ru.mail.sporttogether.activities.LoginActivity
import ru.mail.sporttogether.auth.core.ISocialNetwork
import ru.mail.sporttogether.auth.core.SocialPerson
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnRequestSocialPersonCompleteListener


/**
 * Created by said on 17.11.16
 */

class GoogleSocialNetwork(activity: AppCompatActivity) : ISocialNetwork, GoogleApiClient.OnConnectionFailedListener {

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

    private val googleApiClient = GoogleApiClient.Builder(activity)
            .enableAutoManage(activity, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

    init {

    }

    override val id: Int
        get() = ID

    override val token: String
        get() = ""

    override fun tryAutoLogin(onLoginCompleteListener: OnLoginCompleteListener) = if (isConnected) {
        onLoginCompleteListener.onSuccess(GoogleSocialNetwork.ID)
        true
    } else false


    override fun getLoadedSocialPerson(): SocialPerson? {
        return null
    }

    override fun logout() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback { status ->

        }
    }

    override fun login(activity: AppCompatActivity, onLoginCompleteListener: OnLoginCompleteListener) {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onDestroy() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === LoginActivity.RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
            return
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        Log.d(LoginActivity.TAG, "handleSignInResult:" + result.isSuccess)
        if (result.isSuccess) {
            val acct = result.signInAccount

        } else {

        }
    }

    override val isConnected: Boolean
        get() {
            val opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient)
            return opr.isDone
        }


    override fun requestPerson(onRequestSocialPersonCompleteListener: OnRequestSocialPersonCompleteListener) {

    }

    companion object {
        @JvmStatic private val RC_SIGN_IN = 1824
        @JvmStatic val ID = 3
    }
}
