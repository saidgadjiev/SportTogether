package ru.mail.sporttogether.auth.core.social_networks

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.LoginEvent
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKAccessTokenTracker
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKScopes
import org.json.JSONException
import org.json.JSONObject
import ru.mail.sporttogether.BuildConfig
import ru.mail.sporttogether.auth.core.ISocialNetwork
import ru.mail.sporttogether.auth.core.SocialNetworkError
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.auth.core.SocialPerson
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnRequestSocialPersonCompleteListener

/**
 * Created by said on 17.11.16
 */

class VKSocialNetwork(activity: Activity) : ISocialNetwork {
    private var onLoginCompleteListener: OnLoginCompleteListener? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val scopes: Array<String>
    private val ACCESS_TOKEN = "VKAccessToken"
    private var socialPerson: SocialPerson? = null

    override val isConnected: Boolean
        get() = !sharedPreferences.getString(ACCESS_TOKEN, "").isNullOrEmpty()

    internal var vkAccessTokenTracker: VKAccessTokenTracker = object : VKAccessTokenTracker() {
        override fun onVKAccessTokenChanged(oldToken: VKAccessToken?, newToken: VKAccessToken?) {
            newToken?.let {
                sharedPreferences
                        .edit()
                        .putString(ACCESS_TOKEN, newToken.accessToken)
                        .apply()
            }
        }
    }

    init {
        this.sharedPreferences = activity.getSharedPreferences(SocialNetworkManager.SHARED_PREFERCE_TAG, Context.MODE_PRIVATE)
        this.scopes = arrayOf(VKScopes.OFFLINE, VKScopes.WALL, VKScopes.FRIENDS)
        vkAccessTokenTracker.startTracking()
    }


    override fun logout() {
        VKSdk.logout()
        sharedPreferences
                .edit()
                .putString(ACCESS_TOKEN, null)
                .apply()
        Log.d(TAG, "VK sdk logout")
    }

    override fun login(activity: AppCompatActivity, onLoginCompleteListener: OnLoginCompleteListener) {
        this.onLoginCompleteListener = onLoginCompleteListener
        VKSdk.login(activity, *scopes)
    }

    override fun onDestroy() {
        vkAccessTokenTracker.stopTracking()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
            override fun onResult(res: VKAccessToken) {
                onLoginCompleteListener!!.onSuccess(ID)
                if (!BuildConfig.DEBUG) Answers.getInstance().logLogin(LoginEvent().putMethod("VK").putSuccess(true))
            }

            override fun onError(error: VKError) {
                onLoginCompleteListener!!.onError(SocialNetworkError(error.errorMessage, error.errorCode))
            }
        })
    }

    override fun requestPerson(onRequestSocialPersonCompleteListener: OnRequestSocialPersonCompleteListener) {
        if (!isConnected) {
            onRequestSocialPersonCompleteListener.onError(SocialNetworkError("Please loggin first", -1))
            return
        }

        val request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,photo_100"))

        request.useSystemLanguage = true
        request.executeWithListener(object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse) {
                super.onComplete(response)

                socialPerson = SocialPerson()
                try {
                    val jsonResponse = response.json.getJSONArray("response").getJSONObject(0)
                    getSocialPerson(socialPerson!!, jsonResponse)
                    onRequestSocialPersonCompleteListener.onComplete(socialPerson!!, ID)
                    Log.d(TAG, jsonResponse.toString())
                } catch (e: JSONException) {
                    Log.e(TAG, e.message, e)
                }

            }

            override fun attemptFailed(request: VKRequest, attemptNumber: Int, totalAttempts: Int) {

            }

            override fun onError(error: VKError) {
                onRequestSocialPersonCompleteListener.onError(SocialNetworkError(error.errorMessage, error.errorCode))
            }
        })
    }

    @Throws(JSONException::class)
    private fun getSocialPerson(socialPerson: SocialPerson, user: JSONObject): SocialPerson {

        if (user.has("id")) {
            socialPerson.id = "vk|" + user.getString("id")
        }
        if (user.has("first_name")) {
            socialPerson.name = user.getString("first_name") + ' ' + user.getString("last_name")
        }
        if (user.has("email")) {
            socialPerson.email = user.getString("email")
        }
        if (user.has("photo_100")) {
            socialPerson.avatarURL = user.getString("photo_100")
        }
        return socialPerson
    }

    override val id: Int
        get() = ID

    override val token: String
        get() = sharedPreferences.getString(ACCESS_TOKEN, "")


    override fun tryAutoLogin(onLoginCompleteListener: OnLoginCompleteListener): Boolean {
        if (isConnected) {
            onLoginCompleteListener.onSuccess(ID)

            return true
        }

        return false
    }

    companion object {
        private val TAG = "VKSocialNetwork"
        val ID = 2
    }
}
