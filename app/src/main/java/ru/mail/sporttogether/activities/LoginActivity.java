package ru.mail.sporttogether.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.lock.AuthButtonSize;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.InitialScreen;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.UsernameStyle;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;

import java.util.ArrayList;
import java.util.List;

import ru.mail.sporttogether.R;
import ru.mail.sporttogether.mvp.presenters.LoginActivityPresenter;
import ru.mail.sporttogether.net.models.User;

public class LoginActivity extends AppCompatActivity {

    private Lock lock;
    private AuthenticationAPIClient aClient;
    LoginActivityPresenter presenter;
    private Auth0 auth0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        final Lock.Builder builder = Lock.newBuilder(auth0, callback);

        presenter = new LoginActivityPresenter(this);
        aClient = new AuthenticationAPIClient(auth0);
        builder.closable(true);
        builder.withAuthButtonSize(AuthButtonSize.SMALL);
        builder.withUsernameStyle(UsernameStyle.USERNAME);
        builder.allowLogIn(true);
        builder.allowSignUp(true);
        builder.allowForgotPassword(true);
        builder.initialScreen(InitialScreen.LOG_IN);
        builder.allowedConnections(generateConnections());
        builder.setDefaultDatabaseConnection("Username-Password-Authentication");
        lock = builder.build(this);
        startActivity(lock.newIntent(this));
    }

    private List<String> generateConnections() {
        List<String> connections = new ArrayList<>();

        connections.add("vkontakte");
        connections.add("Username-Password-Authentication");
        if (connections.isEmpty()) {
            connections.add("no-connection");
        }

        return connections;
    }

    public void signin(final String idToken) {
        aClient.tokenInfo(idToken)
                .start(new BaseCallback<UserProfile, AuthenticationException>() {
                    @Override
                    public void onSuccess(final UserProfile payload) {
                        presenter.onSuccess(new User(idToken, payload.getId()));
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                    }
                });
    }

    private LockCallback callback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
            //App.Companion.getInstance().setCredentials(credentials);
            signin(credentials.getIdToken());
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
}
