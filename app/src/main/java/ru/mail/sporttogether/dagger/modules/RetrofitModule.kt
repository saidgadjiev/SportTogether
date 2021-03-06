package ru.mail.sporttogether.dagger.modules

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.mail.sporttogether.BuildConfig
import ru.mail.sporttogether.managers.HeaderManagerImpl
import ru.mail.sporttogether.net.adapters.GeoObjectListAdapterDeserializer
import ru.mail.sporttogether.net.interceptors.SportInterceptor
import ru.mail.sporttogether.net.models.yandex.maps.GeoObject
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by bagrusss on 09.10.16
 */
@Module
class RetrofitModule {

    companion object {
        @JvmStatic val BASE_URL = "http://p30281.lab1.stud.tech-mail.ru/"
        @JvmStatic val YANDEX_URL = "https://geocode-maps.yandex.ru/"

        @JvmStatic val READ_TIMEOUT = 30L
        @JvmStatic val WRITE_TIMEOUT = 30L
        @JvmStatic val CONNECT_TIMEOUT = 30L
    }


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
    fun provideGson() = GsonBuilder()
            .setLenient()
            .registerTypeAdapter(object : TypeToken<ArrayList<GeoObject>>() {}.type, GeoObjectListAdapterDeserializer())
            .create()

    @Singleton
    @Provides
    @Named("no_auth")
    fun provideYandexRetrofit(gson: Gson): Retrofit {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE

        val okBuilder = OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logInterceptor)
        okBuilder.networkInterceptors().add(StethoInterceptor())

        return Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okBuilder.build())
                .baseUrl(YANDEX_URL)
                .build()

    }

}