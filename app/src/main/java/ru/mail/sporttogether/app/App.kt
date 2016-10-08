package ru.mail.sporttogether.app

import android.support.multidex.MultiDexApplication
import com.facebook.stetho.Stetho


/**
 * Created by said on 19.09.16.
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        //TODO подключать, если не релизная версия
        Stetho.initializeWithDefaults(this)
    }
}
