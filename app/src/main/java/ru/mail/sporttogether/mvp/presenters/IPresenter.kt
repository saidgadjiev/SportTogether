package ru.mail.sporttogether.mvp.presenters

import android.os.Bundle

/**
 * Created by bagrusss on 01.10.16.
 *
 */
interface IPresenter {

    fun onCreate(args: Bundle?){}

    fun onDestroy(){}

    fun onPause(){}

    fun onResume(){}

    fun onStart(){}

    fun onStop(){}

    fun onLowMemory(){}

    fun onSaveInstanceState(outState: Bundle?){}

    fun onRestoreInstanceState(savedInstanceState: Bundle?){}

    fun onBackPressed(){}

}