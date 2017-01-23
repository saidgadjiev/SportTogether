package ru.mail.sporttogether.auth.core.social_networks

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import org.json.JSONException
import org.json.JSONObject
import ru.mail.sporttogether.R
import ru.mail.sporttogether.auth.core.ISocialNetwork
import ru.mail.sporttogether.auth.core.SocialNetworkError
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.auth.core.SocialPerson
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnRequestSocialPersonCompleteListener


/**
 * Created by said on 12.11.16.
 */

class FacebookSocialNetwork(private val activity: Activity) : ISocialNetwork {
    private val permissions: Collection<String>
    private var onLoginCompleteListener: OnLoginCompleteListener? = null
    private val callbackManager: CallbackManager
    private val sharedPreferences: SharedPreferences
    private val ACCESS_TOKEN = "FacebookAccessToken"
    private var socialPerson: SocialPerson? = null

    override val id: Int
        get() = ID

    override val token: String
        get() = sharedPreferences.getString(ACCESS_TOKEN, "")

    override val isConnected: Boolean
        get() = !sharedPreferences.getString(ACCESS_TOKEN, "").isEmpty()

    init {
        this.sharedPreferences = activity.getSharedPreferences(SocialNetworkManager.SHARED_PREFERCE_TAG, Context.MODE_PRIVATE)
        this.callbackManager = CallbackManager.Factory.create()
        this.permissions = setOf("public_profile")
        initializeSdk()
    }

    private fun initializeSdk() {
        if (FacebookSdk.isInitialized()) {
            Log.d(TAG, "Facebook sdk already initialized")
        } else {
            val initializeCallback: FacebookSdk.InitializeCallback = FacebookSdk.InitializeCallback {
                Log.d("#MY " + javaClass.simpleName, "sdk init completed")
            }
            FacebookSdk.sdkInitialize(activity, initializeCallback)
            Log.d(TAG, "Facebook sdk initialize")
        }
        val currentToken1 = AccessToken.getCurrentAccessToken()
        Log.d("#MY " + javaClass.simpleName, "current token after sdk init : " + currentToken1)
    }

    override fun setOnLoginCompleteListener(onLoginCompleteListener: OnLoginCompleteListener) {

    }

    override fun onDestroy() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun logout() {
        if (FacebookSdk.isInitialized()) {
            LoginManager.getInstance().logOut()
            sharedPreferences
                    .edit()
                    .putString(ACCESS_TOKEN, null)
                    .apply()

            Log.d(TAG, "Facebook sdk logout")
        } else {
            Log.w(TAG, "Couldn't log out as the SDK wasn't initialized yet.")
        }
    }

    override fun login(activity: Activity, onLoginCompleteListener: OnLoginCompleteListener) {
        Log.d("#MY " + javaClass.simpleName, "Facebook start logging")
        Log.d("#MY " + javaClass.simpleName, "on login complete listener : " + onLoginCompleteListener.javaClass.simpleName)
        this.onLoginCompleteListener = onLoginCompleteListener
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d("#MY " + javaClass.simpleName, "Facebook success logging")
                sharedPreferences
                        .edit()
                        .putString(ACCESS_TOKEN, result.accessToken.token)
                        .apply()
                this@FacebookSocialNetwork.onLoginCompleteListener!!.onSuccess(ID)
            }

            override fun onCancel() {
                Log.d("#MY " + javaClass.simpleName, "Facebook logging on cancel")
                this@FacebookSocialNetwork.onLoginCompleteListener!!.onCancel()
            }

            override fun onError(error: FacebookException) {
                this@FacebookSocialNetwork.onLoginCompleteListener!!.onError(SocialNetworkError(error.message!!, -1))
                Log.d(TAG, "Facebook sdk error")
            }
        })
        LoginManager.getInstance().logInWithReadPermissions(activity, permissions)
    }

    override fun requestPerson(onRequestSocialPersonCompleteListener: OnRequestSocialPersonCompleteListener) {
        Log.d("#MY " + javaClass.simpleName, "fb request person started. is connected? : " + isConnected)
        if (!isConnected) {
            onRequestSocialPersonCompleteListener.onError(SocialNetworkError("Please login first", -1))

            return
        }
        val currentAccessToken = AccessToken.getCurrentAccessToken()
        Log.d("#MY " + javaClass.simpleName, "fb request person started. current access token : " + currentAccessToken)
        val graphRequest =
                GraphRequest.newMeRequest(currentAccessToken) { `object`, response ->
                    if (response.error != null && response.error.requestStatusCode == 400) {
                        Log.d("#MY " + javaClass.simpleName, "fb request person error")
                        onRequestSocialPersonCompleteListener.onError(SocialNetworkError("fb 400 error", -1))
                    } else {
                        socialPerson = SocialPerson()
                        try {
                            getSocialPerson(socialPerson!!, `object`)
                        } catch (e: Throwable) {
                            AlertDialog.Builder(activity)
                                    .setMessage(R.string.network_error)
                                    .setPositiveButton(android.R.string.ok, { dialog, which ->

                                    })
                                    .create()
                                    .show()
                            return@newMeRequest
                            //onRequestSocialPersonCompleteListener.onError(SocialNetworkError(e.message!!, -1))
                        }

                        onRequestSocialPersonCompleteListener.onComplete(socialPerson!!, ID)
                    }

                }
        val parameters = Bundle()

        parameters.putString("fields", "id,name,email")
        graphRequest.parameters = parameters
        graphRequest.executeAsync()
    }

    @Throws(JSONException::class)
    private fun getSocialPerson(socialPerson: SocialPerson, user: JSONObject): SocialPerson {

        if (user.has("id")) {
            socialPerson.id = user.getString("id")
            socialPerson.avatarURL = String.format("https://graph.facebook.com/%s/picture?type=large", user.getString("id"))
        }
        if (user.has("name")) {
            socialPerson.name = user.getString("name")
        }
        if (user.has("email")) {
            socialPerson.email = user.getString("email")
        }
        return socialPerson
    }

    override fun sharePost(activity: Activity, title: String, description: String, uri: String) {
        val content = ShareLinkContent.Builder()
                .setContentDescription(description)
                .setContentTitle(title)
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build()
        val shareDialog = ShareDialog(activity)

        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC)
    }

    override fun getLoadedSocialPerson(): SocialPerson? {
        return socialPerson
    }

    override fun tryAutoLogin(onLoginCompleteListener: OnLoginCompleteListener): Boolean {
        if (isConnected) {
            onLoginCompleteListener.onSuccess(FacebookSocialNetwork.ID)

            return true
        }
        return false
    }

    companion object {
        private val TAG = "#MY "
        val ID = 1
    }
}
