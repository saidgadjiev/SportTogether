package ru.mail.sporttogether.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.facebook.stetho.Stetho
import com.vk.sdk.VKSdk
import ru.mail.sporttogether.BuildConfig
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.dagger.components.AppMainComponent
import ru.mail.sporttogether.dagger.components.DaggerAppMainComponent


/**
 * Created by said on 19.09.16.
 *
 */
class App : Application() {

    companion object {
        lateinit var sharedPreferences: SharedPreferences
        lateinit var injector: AppMainComponent
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences(SocialNetworkManager.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        VKSdk.initialize(this)
        context = this
        injector = DaggerAppMainComponent
                .builder()
                .build()
        //TODO подключать, если не релизная версия
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }
}
