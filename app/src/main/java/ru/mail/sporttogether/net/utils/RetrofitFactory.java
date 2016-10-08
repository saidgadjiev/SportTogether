package ru.mail.sporttogether.net.utils;


import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mail.sporttogether.managers.HeaderManager;
import ru.mail.sporttogether.net.adapter.UserAdapter;
import ru.mail.sporttogether.net.api.RestAPI;
import ru.mail.sporttogether.net.interceptors.SportInterceptor;
import ru.mail.sporttogether.net.models.User;

/**
 * Created by said on 01.10.16.
 */
public class RetrofitFactory {

    private static final String BASE_URL = "http://p30281.lab1.stud.tech-mail.ru/";
    private static final int READ_TIMEOUT = 30;
    private static final int CONNECT_TIMEOUT = 10;
    public static final RestAPI API = newInstance().create(RestAPI.class);

    private static Retrofit newInstance() {
        OkHttpClient.Builder okBuiler = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new SportInterceptor(HeaderManager.INSTANCE));
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(User.class, new UserAdapter());

        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .client(okBuiler.build())
                .baseUrl(BASE_URL)
                .build();
    }
}
