package ru.mail.sporttogether.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.auth0.android.lock.Lock;

import ru.mail.sporttogether.mvp.presenters.auth.ILoginPresenter;
import ru.mail.sporttogether.mvp.presenters.auth.LoginActivityPresenterImpl;
import ru.mail.sporttogether.mvp.views.login.ILoginView;

public class LoginActivity extends PresenterActivity<ILoginPresenter> implements ILoginView {

    private ILoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new LoginActivityPresenterImpl(this);
        presenter.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        presenter = null;
    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(LoginActivity.this, DrawerActivity.class));
        finish();
    }

    @Override
    public void startLockActivity(@NonNull Lock lock) {
        startActivity(lock.newIntent(this));
    }
}
