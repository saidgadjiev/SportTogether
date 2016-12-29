package ru.mail.sporttogether.dagger.modules

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.mail.sporttogether.net.api.*
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by bagrusss on 30.09.16
 */
@Module
class RestModule {

    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthorizationAPI =
            retrofit.create(AuthorizationAPI::class.java)

    @Singleton
    @Provides
    fun provideEventsApi(retrofit: Retrofit): EventsAPI =
            retrofit.create(EventsAPI::class.java)

    @Singleton
    @Provides
    fun provideCategoriesApi(retrofit: Retrofit): CategoriesAPI =
            retrofit.create(CategoriesAPI::class.java)

    @Singleton
    @Provides
    fun provideYandexApi(@Named("no_auth") retrofit: Retrofit): YandexMapsApi =
            retrofit.create(YandexMapsApi::class.java)

    @Singleton
    @Provides
    fun provideServiceApi( retrofit: Retrofit): ServiceApi =
            retrofit.create(ServiceApi::class.java)

}