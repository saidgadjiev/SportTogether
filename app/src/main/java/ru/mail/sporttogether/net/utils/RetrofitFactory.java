package ru.mail.sporttogether.net.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mail.sporttogether.net.api.RestAPI;

/**
 * Created by said on 01.10.16.
 */
public class RetrofitFactory {

    private static final String BASE_URL = "http://p30212.lab1.stud.tech-mail.ru/";
    private static final int READ_TIMEOUT = 30;
    private static final int CONNECT_TIMEOUT = 10;

    private static Gson GSON = new GsonBuilder().setLenient().create();
    public static final RestAPI API = newInstance().create(RestAPI.class);


    private static Retrofit newInstance() {
        OkHttpClient.Builder okBuiler = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GSON))
                .client(okBuiler.build())
                .baseUrl(BASE_URL)
                .build();
    }
}
