package ru.mail.sporttogether.mvp.presenters;

import android.content.Intent;

import com.auth0.android.result.Credentials;

import retrofit2.Retrofit;
import ru.mail.sporttogether.activities.LoginActivity;
import ru.mail.sporttogether.activities.MapActivity;
import ru.mail.sporttogether.app.App;
import ru.mail.sporttogether.net.api.RestAPI;
import ru.mail.sporttogether.net.models.User;
import ru.mail.sporttogether.net.responses.Response;
import ru.mail.sporttogether.net.utils.RetrofitFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by said on 05.10.16.
 */

public class LoginActivityPresenter {
    private LoginActivity view;


    public LoginActivityPresenter(LoginActivity view) {
        this.view = view;
    }



    public void onAuthentification(Credentials credentials) {
        App.Companion.getInstance().setCredentials(credentials);
        view.startActivity(new Intent(view, MapActivity.class));
    }

    public void onSuccess(User user) {
        Retrofit retrofit = RetrofitFactory.getInstance();
        RestAPI api = retrofit.create(RestAPI.class);

        api.updateAuthorization(user).
                subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<Response<Object>>() {
                    @Override
                    public void call(Response<Object> objectResponse) {
                        view.startActivity(new Intent(view, MapActivity.class));
                    }
    });
    }
}
