package ru.mail.sporttogether.mvp.presenters

import android.content.Intent
import android.os.Bundle

/**
 * Created by bagrusss on 01.10.16.
 *
 */
interface IPresenter {


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

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

    fun onPermissionsGranted(requestCode:Int){}

    fun onPermissionNotGranted(requestCode: Int){}

}