package ru.mail.sporttogether.dagger.modules

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import retrofit2.Retrofit
import ru.mail.sporttogether.net.api.RestAPI
import javax.inject.Singleton

/**
 * Created by bagrusss on 30.09.16.
 *
 */
@Module
@Singleton
class RestModule {

    @Provides
    fun provideRestApi(retrofit: Retrofit): RestAPI =
            retrofit.create(RestAPI::class.java)

}