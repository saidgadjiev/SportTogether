package ru.mail.sporttogether.mvp.presenters.auth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.facebook.FacebookAuthHandler;
import com.auth0.android.facebook.FacebookAuthProvider;
import com.auth0.android.lock.AuthButtonSize;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.InitialScreen;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.UsernameStyle;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import ru.mail.sporttogether.activities.LoginActivity;
import ru.mail.sporttogether.app.App;
import ru.mail.sporttogether.managers.auth.AuthManager;
import ru.mail.sporttogether.managers.data.ICredentialsManager;
import ru.mail.sporttogether.managers.headers.HeaderManagerImpl;
import ru.mail.sporttogether.mvp.views.login.ILoginView;
import ru.mail.sporttogether.net.api.AuthorizationAPI;

/**
 * Created by said on 05.10.16.
 */
public class LoginActivityPresenter implements ILoginPresenter {
    private ILoginView view;
    @Inject
    AuthorizationAPI api;
    @Inject
    Context context;
    @Inject
    Auth0 auth0;
    @Inject
    AuthenticationAPIClient aClient;
    @Inject
    ICredentialsManager credentialsManager;
    @Inject
    HeaderManagerImpl headerManager;
    @Inject
    AuthManager authManager;
    @Inject
    FacebookAuthProvider provider;
    private Lock lock;
    private static final int RC_PERMISSIONS = 110;
    private static final int RC_AUTHENTICATION = 111;

    public LoginActivityPresenter(final ILoginView view) {
        App.injector.usePresenterComponent().inject(this);
        this.view = view;
    }

    private List<String> generateConnections() {
        List<String> connections = new ArrayList<>();


        connections.add("facebook");
        if (connections.isEmpty()) {
            connections.add("no-connection");
        }

        return connections;
    }

    private void trySignIn() {
        aClient.tokenInfo(credentialsManager.getCredentials(context).getIdToken())
                .start(new BaseCallback<UserProfile, AuthenticationException>() {
                    @Override
                    public void onSuccess(final UserProfile payload) {
                        Log.d("AUTH", "Authomatic login");

                        headerManager.setToken(credentialsManager.getCredentials(context).getIdToken());
                        headerManager.setClientId(payload.getId());
                        view.startMainActivity();
                        authManager.auth(api, view);
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        Log.d("AUTH", "Session expired");

                        credentialsManager.deleteCredentials(context);
                        view.startLockActivity(lock);
                    }
                });
    }

    @Override
    public void onCreate(@Nullable Bundle args) {
        final Lock.Builder builder = Lock.newBuilder(auth0, callback);

        aClient = new AuthenticationAPIClient(auth0);
        provider.rememberLastLogin(false);
        provider.setPermissions(Arrays.asList("public_profile"));

        FacebookAuthHandler handler = new FacebookAuthHandler(provider);
        builder.closable(true);
        builder.withAuthHandlers(handler);
        builder.withAuthButtonSize(AuthButtonSize.SMALL);
        builder.withUsernameStyle(UsernameStyle.USERNAME);
        builder.allowLogIn(true);
        builder.allowSignUp(true);
        builder.allowForgotPassword(true);
        builder.initialScreen(InitialScreen.LOG_IN);
        builder.allowedConnections(generateConnections());
        builder.setDefaultDatabaseConnection("Username-Password-Authentication");
        lock = builder.build((Activity) view);
        if (credentialsManager.getCredentials(context).getIdToken() == null) {
            view.startLockActivity(lock);

            return;
        }
        trySignIn();
    }

    private AuthenticationCallback callback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
            Log.d("AUTH", "Logged in");

            credentialsManager.saveCredentials(context, credentials);
            trySignIn();
        }

        @Override
        public void onCanceled() {
            Log.d("Lock", "User pressed back.");
        }

        @Override
        public void onError(LockException error) {
            Log.d("Lock", "Error");
        }
    };

    @Override
    public void onDestroy() {
        lock.onDestroy((LoginActivity) view);
        lock = null;
        view = null;
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
