package ru.mail.sporttogether.activities.view

import android.app.Activity
import ru.mail.sporttogether.mvp.IView

/**
 * Created by bagrusss on 07.11.16.
 *
 */
interface SplashView : IView {
    fun startMainActivity()
    fun startLoginActivity()
    fun getActivity(): Activity
    fun close()
}