package ru.mail.sporttogether.dagger.modules

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.mail.sporttogether.managers.HeaderManager
import ru.mail.sporttogether.net.interceptors.SportInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by bagrusss on 09.10.16.
 *
 */
@Module
class RetrofitModule {

    private val BASE_URL = "http://p30281.lab1.stud.tech-mail.ru/"
    private val READ_TIMEOUT = 30L
    private val CONNECT_TIMEOUT = 10L

    @Singleton
    @Provides
    fun provideRetrofit(headerManager: HeaderManager): Retrofit {
        val okBuilder = OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(SportInterceptor(headerManager))
                .addInterceptor(StethoInterceptor())
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(okBuilder.build())
                .baseUrl(BASE_URL)
                .build()
    }
}