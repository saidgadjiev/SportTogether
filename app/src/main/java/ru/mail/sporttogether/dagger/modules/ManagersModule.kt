package ru.mail.sporttogether.dagger.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mail.sporttogether.managers.LocationManager
import ru.mail.sporttogether.managers.auth.AuthManager
import ru.mail.sporttogether.managers.data.CredentialsManagerImpl
import ru.mail.sporttogether.managers.data.DataManagerImpl
import ru.mail.sporttogether.managers.data.CredentialsManager
import ru.mail.sporttogether.managers.data.IDataManager
import ru.mail.sporttogether.managers.events.EventsManager
import ru.mail.sporttogether.managers.events.EventsManagerImpl
import ru.mail.sporttogether.managers.headers.HeaderManagerImpl
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
    fun provideCredentialsManager(context: Context): CredentialsManager
            = CredentialsManagerImpl(context)

    @Singleton
    @Provides
    fun provideHeaderManager(): HeaderManagerImpl
            = HeaderManagerImpl()

    @Singleton
    @Provides
    fun provideAuthManager(manager: CredentialsManager): AuthManager
            = AuthManager(manager)

    @Singleton
    @Provides
    fun provideEventsManager(): EventsManager
            = EventsManagerImpl()

}