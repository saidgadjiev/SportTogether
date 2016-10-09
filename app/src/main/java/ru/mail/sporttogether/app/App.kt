package ru.mail.sporttogether.app

import android.support.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import ru.mail.sporttogether.dagger.components.AppMainComponent
import ru.mail.sporttogether.dagger.components.DaggerAppMainComponent


/**
 * Created by said on 19.09.16.
 */
class App : MultiDexApplication() {

    companion object{
        lateinit var injector: AppMainComponent
    }

    override fun onCreate() {
        super.onCreate()

        injector = DaggerAppMainComponent
                .builder()
                .build()
        //TODO подключать, если не релизная версия
        Stetho.initializeWithDefaults(this)
    }
}
