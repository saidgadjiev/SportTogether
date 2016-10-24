package ru.mail.sporttogether.mvp.presenters.auth.auth0provider.vkprovider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.AuthenticationCallback;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.AuthProvider;
import com.auth0.android.result.Credentials;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKError;

import java.util.Collection;

/**
 * Created by said on 22.10.16.
 */

public class VKAuthProvider extends AuthProvider {

    private static final String TAG = VKAuthProvider.class.getSimpleName();
    private AuthenticationAPIClient auth0;
    private VKApi vkApi;
    private String connectionName;
    private Collection<String> permissions;
    private boolean rememberLastLogin;

    public VKAuthProvider(@NonNull AuthenticationAPIClient auth0) {
        this("vkontakte", auth0);
    }

    private VKAuthProvider(String connectionName, AuthenticationAPIClient auth0) {
        this(connectionName, auth0, new VKApi());
    }

    private VKAuthProvider(String connectionName, AuthenticationAPIClient auth0, VKApi vkApi) {
        this.connectionName = connectionName;
        this.auth0 = auth0;
        this.vkApi = vkApi;
    }

    public void rememberLastLogin(boolean rememberLastLogin) {
        this.rememberLastLogin = rememberLastLogin;
    }

    public void setPermissions(@NonNull Collection<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    protected void requestAuth(Activity activity, int requestCode) {
        if (rememberLastLogin) {
            vkApi.logout();
        }
        vkApi.login(activity, requestCode, permissions, createVKCallback());
    }

    @Override
    public boolean authorize(int requestCode, int resultCode, @Nullable Intent intent) {
        return vkApi.finishLogin(requestCode, resultCode, intent);
    }

    @Override
    public void stop() {
        super.stop();
        clearSession();
    }

    @Override
    public void clearSession() {
        super.clearSession();
        vkApi.logout();
    }

    Collection<String> getPermissions() {
        return permissions;
    }

    String getConnection() {
        return connectionName;
    }

    @Override
    public boolean authorize(@Nullable Intent intent) {
        return false;
    }

    @Override
    public String[] getRequiredAndroidPermissions() {
        return new String[0];
    }

    private void requestAuth0Token(String token) {
        final AuthCallback callback = getSafeCallback();
        auth0.loginWithOAuthAccessToken(token, connectionName)
                .start(new AuthenticationCallback<Credentials>() {
                    @Override
                    public void onSuccess(Credentials credentials) {
                        callback.onSuccess(credentials);
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        callback.onFailure(error);
                    }
                });
    }

    private VKApi.Callback createVKCallback() {
        final AuthCallback callback = getSafeCallback();
        return new VKApi.Callback() {
            @Override
            public void onSuccess(VKAccessToken accessToken) {
                requestAuth0Token(accessToken.accessToken);
            }

            @Override
            public void onCancel() {
                Log.w(TAG, "User cancelled the log in dialog");
                callback.onFailure(new AuthenticationException("User cancelled the authentication consent dialog."));
            }

            @Override
            public void onError(VKError error) {
                Log.e(TAG, "Error on log in: " + error.errorMessage);
                callback.onFailure(new AuthenticationException(error.errorMessage));
            }
        };
    }

    private AuthCallback getSafeCallback() {
        final AuthCallback callback = getCallback();
        return callback != null ? callback : new AuthCallback() {
            @Override
            public void onFailure(@NonNull Dialog dialog) {
                Log.w(TAG, "Called authorize with no callback defined");
            }

            @Override
            public void onFailure(AuthenticationException exception) {
                Log.w(TAG, "Called authorize with no callback defined");
            }

            @Override
            public void onSuccess(@NonNull Credentials credentials) {
                Log.w(TAG, "Called authorize with no callback defined");
            }
        };
    }
}
