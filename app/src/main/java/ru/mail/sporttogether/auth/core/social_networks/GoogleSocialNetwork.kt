package ru.mail.sporttogether.auth.core.social_networks

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import ru.mail.sporttogether.auth.core.ISocialNetwork
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnRequestSocialPersonCompleteListener
import ru.mail.sporttogether.auth.core.persons.SocialPerson


/**
 * Created by said on 17.11.16.
 */

class GoogleSocialNetwork(private val activity: Activity, private val appId: String) : ISocialNetwork {
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

    }

    override val isConnected: Boolean
        get() = false

    override fun sharePost(activity: Activity, title: String, description: String, uri: String) {

    }

    override fun requestPerson(onRequestSocialPersonCompleteListener: OnRequestSocialPersonCompleteListener) {

    }

    override val id: Int?
        get() = null

    override val token: String
        get() = ""
}
