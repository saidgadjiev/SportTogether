package ru.mail.sporttogether.dagger.modules

import dagger.Module
import dagger.Provides
import ru.mail.sporttogether.managers.LocationManager
import ru.mail.sporttogether.managers.auth.AuthManager
import ru.mail.sporttogether.managers.data.*
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
    fun provideLocationManager()
            = LocationManager()

    @Singleton
    @Provides
    fun provideDataManager(): IDataManager
            = DataManagerImpl()

    @Singleton
    @Provides
    fun provideCredentialsManager(): ICredentialsManager
            = CredentialsManagerImpl()

    @Singleton
    @Provides
    fun provideHeaderManager(locationManager: LocationManager)
            = HeaderManagerImpl(locationManager)

    @Singleton
    @Provides
    fun provideAuthManager(): AuthManager
            = AuthManager()

    @Provides
    @Singleton
    fun provideNotificationManager()
            = NotificationManager()


    @Provides
    @Singleton
    fun provideFCMTokenManager()
            = FcmTokenManager()
}