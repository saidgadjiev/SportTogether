package ru.mail.sporttogether.app

import android.app.Application
import android.content.Context
import com.auth0.android.Auth0
import com.facebook.stetho.Stetho
import ru.mail.sporttogether.BuildConfig
import ru.mail.sporttogether.R
import ru.mail.sporttogether.dagger.components.AppMainComponent
import ru.mail.sporttogether.dagger.components.DaggerAppMainComponent
import ru.mail.sporttogether.dagger.modules.Auth0Module


/**
 * Created by said on 19.09.16.
 *
 */
class App : Application() {

    companion object {
        lateinit var injector: AppMainComponent
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        injector = DaggerAppMainComponent
                .builder()
                .auth0Module(Auth0Module(Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain))))
                .build()
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }
}
