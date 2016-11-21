package ru.mail.sporttogether.app

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.vk.sdk.VKSdk
import ru.mail.sporttogether.BuildConfig
import ru.mail.sporttogether.dagger.components.AppMainComponent
import ru.mail.sporttogether.dagger.components.DaggerAppMainComponent


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
