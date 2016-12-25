package ru.mail.sporttogether.auth.core

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnRequestSocialPersonCompleteListener
import ru.mail.sporttogether.auth.core.persons.SocialPerson


/**
 * Created by said on 12.11.16.
 */

interface ISocialNetwork {

    fun setOnLoginCompleteListener(onLoginCompleteListener: OnLoginCompleteListener)

    fun tryAutoLogin(onLoginCompleteListener: OnLoginCompleteListener): Boolean

    fun logout()

    fun login(activity: Activity, onLoginCompleteListener: OnLoginCompleteListener)

    fun onStart()

    fun onPause()

    fun onResume()

    fun onStop()

    fun onDestroy()

    fun onSaveInstanceState(outState: Bundle)

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    val isConnected: Boolean

    fun sharePost(activity: Activity, title: String, description: String, uri: String)

    fun requestPerson(onRequestSocialPersonCompleteListener: OnRequestSocialPersonCompleteListener)

    val id: Int

    val token: String
    fun getLoadedSocialPerson(): SocialPerson?
}
