package ru.mail.sporttogether.auth.core.social_networks

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import org.json.JSONException
import org.json.JSONObject
import ru.mail.sporttogether.auth.core.ISocialNetwork
import ru.mail.sporttogether.auth.core.SocialNetworkError
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnRequestSocialPersonCompleteListener
import ru.mail.sporttogether.auth.core.persons.SocialPerson


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

    init {
        this.sharedPreferences = activity.getSharedPreferences(SocialNetworkManager.SHARED_PREFERCE_TAG, Context.MODE_PRIVATE)
        this.callbackManager = CallbackManager.Factory.create()
        this.permissions = setOf("public_profile")
        initializeSdk()
    }

    override fun setOnLoginCompleteListener(onLoginCompleteListener: OnLoginCompleteListener) {

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
        this.onLoginCompleteListener = onLoginCompleteListener
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                this@FacebookSocialNetwork.onLoginCompleteListener!!.onSuccess(ID)
                sharedPreferences
                        .edit()
                        .putString(ACCESS_TOKEN, result.accessToken.token)
                        .apply()
                Log.d(TAG, "Facebook sdk login")
            }

            override fun onCancel() {
                this@FacebookSocialNetwork.onLoginCompleteListener!!.onCancel()
            }

            override fun onError(error: FacebookException) {
                this@FacebookSocialNetwork.onLoginCompleteListener!!.onError(SocialNetworkError(error.message!!, -1))
                Log.d(TAG, "Facebook sdk error")
            }
        })
        LoginManager.getInstance().logInWithReadPermissions(activity, permissions)
    }

    private fun initializeSdk() {
        if (FacebookSdk.isInitialized()) {
            Log.d(TAG, "Facebook sdk already initialized")
        } else {
            FacebookSdk.sdkInitialize(activity)
            Log.d(TAG, "Facebook sdk initialize")
        }
    }

    override fun onStart() {

    }

    override fun onPause() {

    }

    override fun onResume() {

    }

    override fun onStop() {

    }

    override fun onDestroy() {

    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override val isConnected: Boolean
        get() = !sharedPreferences.getString(ACCESS_TOKEN, "")!!.isEmpty()

    override fun requestPerson(onRequestSocialPersonCompleteListener: OnRequestSocialPersonCompleteListener) {
        if (!isConnected) {
            onRequestSocialPersonCompleteListener.onError(SocialNetworkError("Please loggin first", -1))

            return
        }
        val graphRequest = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken()) { `object`, response ->
            Log.d(TAG, `object`.toString())
            socialPerson = SocialPerson()
            try {
                getSocialPerson(socialPerson!!, `object`)
            } catch (e: JSONException) {
                e.printStackTrace()
                onRequestSocialPersonCompleteListener.onError(SocialNetworkError(e.message!!, -1))
            }

            onRequestSocialPersonCompleteListener.onComplete(socialPerson!!, ID)
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
            onLoginCompleteListener.onSuccess(VKSocialNetwork.ID)

            return true
        }
        return false
    }

    override val id: Int?
        get() = ID

    override val token: String
        get() = sharedPreferences.getString(ACCESS_TOKEN, "")

    companion object {
        private val TAG = "FacebookSocialNetwork"
        val ID = 1
    }
}
