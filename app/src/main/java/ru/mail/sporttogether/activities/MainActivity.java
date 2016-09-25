package ru.mail.sporttogether.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.auth0.android.Auth0;
import com.auth0.android.lock.AuthButtonSize;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.InitialScreen;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.UsernameStyle;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;

import java.util.ArrayList;
import java.util.List;

import ru.mail.sporttogether.R;

public class MainActivity extends AppCompatActivity {

    private Lock lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Lock.Builder builder = Lock.newBuilder(getAccount(), callback);

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

    private Auth0 getAccount() {
        return new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
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

    private LockCallback callback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
            Log.d("Lock", "OK");
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
