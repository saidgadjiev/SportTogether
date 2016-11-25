package ru.mail.sporttogether.dagger.modules

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.mail.sporttogether.BuildConfig
import ru.mail.sporttogether.managers.headers.HeaderManagerImpl
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
    private val WRITE_TIMEOUT = 30L
    private val CONNECT_TIMEOUT = 30L

    @Singleton
    @Provides
    fun provideRetrofit(headerManager: HeaderManagerImpl, gson: Gson): Retrofit {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
        val okBuilder = OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logInterceptor)
                .addInterceptor(SportInterceptor(headerManager))
        okBuilder.networkInterceptors().add(StethoInterceptor())

        return Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okBuilder.build())
                .baseUrl(BASE_URL)
                .build()
    }

    @Provides
    @Singleton
    fun provideGson()
            = GsonBuilder().setLenient().create()

}