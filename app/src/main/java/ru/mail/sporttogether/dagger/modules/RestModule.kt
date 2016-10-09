package ru.mail.sporttogether.dagger.modules

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.mail.sporttogether.net.api.AuthorizationAPI
import ru.mail.sporttogether.net.api.CategoriesAPI
import ru.mail.sporttogether.net.api.EventsAPI
import javax.inject.Singleton

/**
 * Created by bagrusss on 30.09.16.
 *
 */
@Module
@Singleton
class RestModule {

    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthorizationAPI =
            retrofit.create(AuthorizationAPI::class.java)

    @Provides
    fun provideEventsApi(retrofit: Retrofit): EventsAPI =
            retrofit.create(EventsAPI::class.java)

    @Provides
    fun provideCategoriesApi(retrofit: Retrofit): CategoriesAPI =
            retrofit.create(CategoriesAPI::class.java)
}