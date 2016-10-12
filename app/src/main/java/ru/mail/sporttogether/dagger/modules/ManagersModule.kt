package ru.mail.sporttogether.dagger.modules

import dagger.Module
import dagger.Provides
import ru.mail.sporttogether.managers.CredentialsManager
import ru.mail.sporttogether.managers.DataManager
import ru.mail.sporttogether.managers.HeaderManager
import ru.mail.sporttogether.managers.LocationManager
import javax.inject.Singleton

/**
 * Created by bagrusss on 09.10.16.
 *
 */
@Module
@Singleton
class ManagersModule {

    @Singleton
    @Provides
    fun provideHeaderManager(locationManager: LocationManager)
            = HeaderManager(locationManager)

    @Singleton
    @Provides
    fun provideLocationManager()
            = LocationManager()

    @Singleton
    @Provides
    fun provideDataManager()
            = DataManager()


    fun provideCredentialsManager()
            = CredentialsManager()


}