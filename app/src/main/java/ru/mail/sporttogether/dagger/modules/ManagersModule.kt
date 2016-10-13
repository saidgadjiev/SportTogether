package ru.mail.sporttogether.dagger.modules

import dagger.Module
import dagger.Provides
import ru.mail.sporttogether.managers.LocationManager
import ru.mail.sporttogether.managers.data.CredentialsManagerImpl
import ru.mail.sporttogether.managers.data.DataManagerImpl
import ru.mail.sporttogether.managers.data.ICredentialsManager
import ru.mail.sporttogether.managers.data.IDataManager
import ru.mail.sporttogether.managers.headers.HeaderManagerImpl
import ru.mail.sporttogether.managers.headers.IHeaderManager
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
    fun provideHeaderManager(locationManager: LocationManager): IHeaderManager
            = HeaderManagerImpl(locationManager)

}