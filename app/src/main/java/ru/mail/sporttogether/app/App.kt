//TODO баг с исчезновением адреса в детализации
//TODO баг с отпиской событий карты после открытия детализации
//TODO отображение результата в детализации
//TODO task recyclerview scrollable

package ru.mail.sporttogether.app

import android.content.Context
import android.support.multidex.MultiDexApplication
import android.support.v7.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.facebook.stetho.Stetho
import com.vk.sdk.VKSdk
import io.fabric.sdk.android.Fabric
import ru.mail.sporttogether.BuildConfig
import ru.mail.sporttogether.dagger.components.AppMainComponent
import ru.mail.sporttogether.dagger.components.DaggerAppMainComponent


/**
 * Created by said on 19.09.16
 */
class App : MultiDexApplication() {

    companion object {
        lateinit var injector: AppMainComponent
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        VKSdk.initialize(this)
        context = this
        injector = DaggerAppMainComponent
                .builder()
                .build()
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
        else {
            Fabric.with(this, Crashlytics(), Answers())
        }
    }
}
