package ru.mail.sporttogether.auth.core

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnRequestSocialPersonCompleteListener


/**
 * Created by said on 12.11.16.
 */

interface ISocialNetwork {

    fun setOnLoginCompleteListener(onLoginCompleteListener: OnLoginCompleteListener)

    fun tryAutoLogin(onLoginCompleteListener: OnLoginCompleteListener): Boolean

    fun logout()

    fun login(activity: AppCompatActivity, onLoginCompleteListener: OnLoginCompleteListener)

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    fun onDestroy()

    val isConnected: Boolean

    fun requestPerson(onRequestSocialPersonCompleteListener: OnRequestSocialPersonCompleteListener)

    val id: Int

    val token: String
    fun getLoadedSocialPerson(): SocialPerson?
}
