package ru.mail.sporttogether.mvp.presenters;

import android.util.Log;

import ru.mail.sporttogether.activities.LoginActivity;
import ru.mail.sporttogether.net.models.User;
import ru.mail.sporttogether.net.responses.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by said on 05.10.16.
 */
public class LoginActivityPresenter {
    private LoginActivity view;


    public LoginActivityPresenter(LoginActivity view) {
        this.view = view;
    }

    public void onSuccess(User user) {
        /*api.updateAuthorization(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("exception", e.getMessage(), e);
                    }

                    @Override
                    public void onNext(Response<Object> objectResponse) {
                        Log.d("AUTH", "Succes send token to server");
                    }
                });*/
    }
}
