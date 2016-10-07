package ru.mail.sporttogether.app

import android.support.multidex.MultiDexApplication
import com.auth0.android.result.Credentials
import com.facebook.stetho.Stetho

/**
 * Created by said on 19.09.16.
 */
class App : MultiDexApplication() {

    var credentials: Credentials? = null
        get
        set

    companion object {
        private lateinit var appSingleton:App

        fun getInstance(): App = appSingleton
    }


    override fun onCreate() {
        super.onCreate()
        appSingleton = this
        Stetho.initializeWithDefaults(this)
    }


}
