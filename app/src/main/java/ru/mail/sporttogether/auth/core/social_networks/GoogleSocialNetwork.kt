package ru.mail.sporttogether.auth.core.social_networks

import android.app.Activity
import android.content.Intent
import ru.mail.sporttogether.auth.core.ISocialNetwork
import ru.mail.sporttogether.auth.core.SocialPerson
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnRequestSocialPersonCompleteListener


/**
 * Created by said on 17.11.16
 */

class GoogleSocialNetwork(private val activity: Activity, private val appId: String) : ISocialNetwork {
    override val id: Int
        get() = 10

    override val token: String
        get() = ""

    override fun tryAutoLogin(onLoginCompleteListener: OnLoginCompleteListener): Boolean {
        return false
    }

    override fun getLoadedSocialPerson(): SocialPerson? {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setOnLoginCompleteListener(onLoginCompleteListener: OnLoginCompleteListener) {

    }

    override fun logout() {

    }

    override fun login(activity: Activity, onLoginCompleteListener: OnLoginCompleteListener) {

    }

    override fun onDestroy() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    override val isConnected: Boolean
        get() = false


    override fun requestPerson(onRequestSocialPersonCompleteListener: OnRequestSocialPersonCompleteListener) {

    }
}
