package ru.mail.sporttogether.auth.core.social_networks

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.auth.core.SocialPerson
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnRequestSocialPersonCompleteListener


/**
 * Created by said on 17.11.16
 * это дичайший говнокод, но нет времени рефачить((
 */

class GoogleSocialNetwork(val context: Context) : ISocialNetwork, GoogleApiClient.OnConnectionFailedListener {

    private var onLoginCompleteListener: OnLoginCompleteListener? = null
    private var sharedPreferences: SharedPreferences


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
        get() = sharedPreferences.getString(KEY_TOKEN, "")

    private var acct: GoogleSignInAccount? = null


    init {
        this.sharedPreferences = context.getSharedPreferences(SocialNetworkManager.SHARED_PREFERCE_TAG, Context.MODE_PRIVATE)

    }

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
            putToSharedPreferences(acct)
            onLoginCompleteListener!!.onSuccess(ID)
            if (!BuildConfig.DEBUG) Answers.getInstance().logLogin(LoginEvent().putMethod("Google").putSuccess(true))
        } else {
            onLoginCompleteListener!!.onError(SocialNetworkError(result.status.statusMessage, result.status.statusCode))
        }
    }

    private fun putToSharedPreferences(account: GoogleSignInAccount?) {
        account?.let {
            sharedPreferences.edit()
                    .putString(KEY_ID, "g|" + it.id)
                    .putString(KEY_PHOTO, it.photoUrl.toString())
                    .putString(KEY_NAME, it.displayName)
                    .putString(KEY_TOKEN, it.email)
                    .commit()
        }
    }

    override val isConnected: Boolean
        get() = !sharedPreferences.getString(KEY_ID, "").isNullOrEmpty()


    override fun requestPerson(onRequestSocialPersonCompleteListener: OnRequestSocialPersonCompleteListener) {
        val person = SocialPerson()
        person.id = sharedPreferences.getString(KEY_ID, "")
        if (!person.id.isNullOrEmpty()) {
            person.avatarURL = sharedPreferences.getString(KEY_PHOTO, "")
            person.name = sharedPreferences.getString(KEY_NAME, "")
            person.email = sharedPreferences.getString(KEY_TOKEN, "")
            onRequestSocialPersonCompleteListener.onComplete(person, ID)
            return
        }
        onRequestSocialPersonCompleteListener.onError(SocialNetworkError("Not exists", -1))
    }

    companion object {
        @JvmStatic private val RC_SIGN_IN = 1824
        @JvmStatic val ID = 3
        @JvmStatic private val TAG = "GoogleSocialNetwork"

        @JvmStatic private val KEY_ID = "KEY_ID"
        @JvmStatic private val KEY_PHOTO = "KEY_PHOTO"
        @JvmStatic private val KEY_NAME = "KEY_NAME"
        @JvmStatic private val KEY_TOKEN = "KEY_TOKEN"
    }
}
