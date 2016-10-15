package ru.mail.sporttogether.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.auth0.android.lock.Lock;

import org.jetbrains.annotations.NotNull;

import ru.mail.sporttogether.mvp.presenters.auth.ILoginPresenter;
import ru.mail.sporttogether.mvp.presenters.auth.LoginActivityPresenter;
import ru.mail.sporttogether.mvp.views.login.ILoginView;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    ILoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new LoginActivityPresenter(this);
        presenter.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        presenter = null;
    }

    @Override
    public void showToast(@NotNull String message, int duration) {
    }

    @Override
    public void showToast(int messageRes, int duration) {

    }

    @Override
    public void showSnackbar(@NotNull String message, int duration) {

    }

    @Override
    public void showSnackbar(int messageRes, int duration) {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

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
