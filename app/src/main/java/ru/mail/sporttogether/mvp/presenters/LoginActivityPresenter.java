package ru.mail.sporttogether.mvp.presenters;

import android.content.Intent;
import android.util.Log;

import com.auth0.android.result.Credentials;

import ru.mail.sporttogether.activities.LoginActivity;
import ru.mail.sporttogether.activities.MapActivity;
import ru.mail.sporttogether.app.App;
import ru.mail.sporttogether.net.api.RestAPI;
import ru.mail.sporttogether.net.models.User;
import ru.mail.sporttogether.net.responses.Response;
import ru.mail.sporttogether.net.utils.RetrofitFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by said on 05.10.16.
 *
 */
public class LoginActivityPresenter {
    private LoginActivity view;

    private RestAPI api = RetrofitFactory.API;

    public LoginActivityPresenter(LoginActivity view) {
        this.view = view;
    }

    public void onAuthentification(Credentials credentials) {
        App.Companion.getInstance().setCredentials(credentials);
        view.startActivity(new Intent(view, MapActivity.class));
    }

    public void onSuccess(User user) {
        api.updateAuthorization(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<Object>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("exception", e.getMessage(), e);
                    }

                    @Override
                    public void onNext(Response<Object> objectResponse) {
                        view.startActivity(new Intent(view, MapActivity.class));
                    }

                });
    }
}
