package ru.mail.sporttogether.net.utils;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mail.sporttogether.net.adapter.UserAdapter;
import ru.mail.sporttogether.net.models.User;

/**
 * Created by said on 01.10.16.
 */

public class RetrofitFactory {

    public static final String BASE_URL = "http://p30212.lab1.stud.tech-mail.ru/";


    public static Retrofit getInstance(){
        OkHttpClient.Builder okBuiler = new OkHttpClient.Builder();
        okBuiler.readTimeout(10, TimeUnit.SECONDS);
        okBuiler.connectTimeout(5, TimeUnit.SECONDS);

        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(User.class, new UserAdapter());

        return new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(builder.create())).client(okBuiler.build()).baseUrl(BASE_URL).build();
    }
}
