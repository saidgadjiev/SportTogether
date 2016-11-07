package ru.mail.sporttogether.mvp.views

import com.auth0.android.lock.Lock

/**
 * Created by bagrusss on 07.11.16.
 *
 */
interface ISplashView : IView {
    fun startMainActivity()
    fun startLockActivity(lock: Lock?)
    fun close()
}