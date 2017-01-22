package ru.mail.sporttogether.dagger.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.managers.EventsManagerImpl
import ru.mail.sporttogether.managers.HeaderManagerImpl
import ru.mail.sporttogether.managers.LocationManager
import javax.inject.Singleton

/**
 * Created by bagrusss on 09.10.16
 */
@Module
class ManagersModule {

    @Singleton
    @Provides
    fun provideLocationManager(context: Context)
            = LocationManager(context)


    @Singleton
    @Provides
    fun provideHeaderManager(): HeaderManagerImpl
            = HeaderManagerImpl()

    @Provides
    @Singleton
    fun provideSocialNetworkManager()
            = SocialNetworkManager()

    @Singleton
    @Provides
    fun provideEventsManager(): EventsManager
            = EventsManagerImpl()
}