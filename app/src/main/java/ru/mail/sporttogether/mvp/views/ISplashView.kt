package ru.mail.sporttogether.mvp.views

import android.app.Activity

/**
 * Created by bagrusss on 07.11.16.
 *
 */
interface ISplashView : IView {
    fun startMainActivity()
    fun startLoginActivity()
    fun getActivity(): Activity
    fun close()
}