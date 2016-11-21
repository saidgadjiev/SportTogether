package ru.mail.sporttogether.dagger.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mail.sporttogether.managers.LocationManager
import ru.mail.sporttogether.managers.data.*
import ru.mail.sporttogether.managers.data.CredentialsManagerImpl
import ru.mail.sporttogether.managers.data.DataManagerImpl
import ru.mail.sporttogether.managers.data.IDataManager
import ru.mail.sporttogether.managers.events.EventsManager
import ru.mail.sporttogether.managers.events.EventsManagerImpl
import ru.mail.sporttogether.managers.headers.HeaderManagerImpl
import ru.mail.sporttogether.managers.notification.NotificationManager
import javax.inject.Singleton

/**
 * Created by bagrusss on 09.10.16.
 *
 */
@Module
class ManagersModule {

    @Singleton
    @Provides
    fun provideLocationManager(context: Context)
            = LocationManager(context)

    @Singleton
    @Provides
    fun provideDataManager(): IDataManager
            = DataManagerImpl()

    @Singleton
    @Provides
    fun provideHeaderManager(): HeaderManagerImpl
            = HeaderManagerImpl()

    @Provides
    @Singleton
    fun provideNotificationManager()
            = NotificationManager()


    @Provides
    @Singleton
    fun provideFCMTokenManager()
            = FcmTokenManager()

    @Singleton
    @Provides
    fun provideEventsManager(): EventsManager
            = EventsManagerImpl()
}