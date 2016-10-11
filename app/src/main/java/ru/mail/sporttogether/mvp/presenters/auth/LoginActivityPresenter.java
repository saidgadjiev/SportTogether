package ru.mail.sporttogether.mvp.presenters.auth;

import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

import ru.mail.sporttogether.app.App;
import ru.mail.sporttogether.mvp.presenters.IPresenter;
import ru.mail.sporttogether.mvp.views.IView;
import ru.mail.sporttogether.net.api.AuthorizationAPI;
import ru.mail.sporttogether.net.responses.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by said on 05.10.16.
 */
public class LoginActivityPresenter implements IPresenter {
    private IView view;
    @Inject
    AuthorizationAPI api;

    public LoginActivityPresenter(IView view) {
        App.injector.usePresenterComponent().inject(this);
        this.view = view;
    }

    public void onSuccess() {
        api.updateAuthorization()
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
                });
    }

    @Override
    public void onCreate(@Nullable Bundle args) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle outState) {

    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {

    }
}
