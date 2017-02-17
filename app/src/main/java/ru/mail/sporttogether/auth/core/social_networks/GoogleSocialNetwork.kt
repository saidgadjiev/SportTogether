package ru.mail.sporttogether.auth.core.social_networks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.LoginEvent
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import ru.mail.sporttogether.BuildConfig
import ru.mail.sporttogether.auth.core.ISocialNetwork
import ru.mail.sporttogether.auth.core.SocialNetworkError
import ru.mail.sporttogether.auth.core.SocialPerson
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnRequestSocialPersonCompleteListener


/**
 * Created by said on 17.11.16
 */

class GoogleSocialNetwork(val context: Context) : ISocialNetwork, GoogleApiClient.OnConnectionFailedListener {

    private var onLoginCompleteListener: OnLoginCompleteListener? = null


    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestProfile()
            .build()

    private val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

    override val id: Int
        get() = ID

    override val token: String
        get() = acct?.idToken ?: acct?.email ?: ""

    private var acct: GoogleSignInAccount? = null


    override fun tryAutoLogin(onLoginCompleteListener: OnLoginCompleteListener) =
            if (isConnected) {
                onLoginCompleteListener.onSuccess(GoogleSocialNetwork.ID)
                true
            } else false

    override fun logout() {
        googleApiClient.connect()
        googleApiClient.registerConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
            override fun onConnected(p0: Bundle?) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback { status ->

                }
            }

            override fun onConnectionSuspended(p0: Int) {

            }
        })

    }

    override fun login(activity: AppCompatActivity, onLoginCompleteListener: OnLoginCompleteListener) {
        this.onLoginCompleteListener = onLoginCompleteListener
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onDestroy() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
            return
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess)
        if (result.isSuccess) {
            acct = result.signInAccount
            onLoginCompleteListener!!.onSuccess(ID)
            if (!BuildConfig.DEBUG) Answers.getInstance().logLogin(LoginEvent().putMethod("VK").putSuccess(true))
        } else {
            onLoginCompleteListener!!.onError(SocialNetworkError(result.status.statusMessage, result.status.statusCode))
        }
    }

    override val isConnected: Boolean
        get() {
            val opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient)
            return opr.isDone
        }


    override fun requestPerson(onRequestSocialPersonCompleteListener: OnRequestSocialPersonCompleteListener) {
        acct?.let {
            val person = SocialPerson()
            person.id = "g|" + it.id
            person.avatarURL = it.photoUrl.toString()
            person.name = it.displayName ?: ""
            person.email = it.email
            onRequestSocialPersonCompleteListener.onComplete(person, ID)
            return
        }
        onRequestSocialPersonCompleteListener.onError(SocialNetworkError("Not exists", 122))
    }

    companion object {
        @JvmStatic private val RC_SIGN_IN = 1824
        @JvmStatic val ID = 3
        @JvmStatic val TAG = "GoogleSocialNetwork"
    }
}
