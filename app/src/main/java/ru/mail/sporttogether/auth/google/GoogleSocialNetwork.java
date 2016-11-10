package ru.mail.sporttogether.auth.google;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;

import ru.mail.sporttogether.auth.core.AuthError;
import ru.mail.sporttogether.auth.core.SocialNetwork;
import ru.mail.sporttogether.auth.core.SocialNetworkManager;

/**
 * Created by said on 07.11.16.
 */

public class GoogleSocialNetwork implements SocialNetwork, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = GoogleSocialNetwork.class.getSimpleName();
    static final int REQUEST_RESOLVE_ERROR = 1001;
    public static final int ID = 2;
    private Scope[] scopes;
    private GoogleApiClient googleApiClient;
    private String appID;
    private Activity activity;
    public final static int RC_SIGN_IN = 201;
    private boolean resolvingError;
    private LoginCallback callback;

    public GoogleSocialNetwork(Activity activity, String appID, Scope[] scopes, LoginCallback callback) {
        this.scopes = scopes;
        this.appID = appID;
        this.callback = callback;
        this.activity = activity;
        googleApiClient = createGoogleAPIClient(activity);
    }

    @Override
    public boolean isLoginOK(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            resolvingError = false;
            if (resultCode == Activity.RESULT_OK) {
                connectAndRequestGoogleAccount(RC_SIGN_IN, REQUEST_RESOLVE_ERROR);
            }
            return true;
        } else if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                final GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    callback.onOK();
                } else if (result.getStatus().isCanceled()) {
                    callback.onCancel();
                }
            }
            return true;
        }

        return false;
    }

    private GoogleApiClient createGoogleAPIClient(Activity activity) {
        final GoogleSignInOptions.Builder gsoBuilder = new GoogleSignInOptions.Builder()
                .requestIdToken(appID);
        if (scopes.length == 1) {
            gsoBuilder.requestScopes(scopes[0]);
        } else if (scopes.length > 1) {
            gsoBuilder.requestScopes(scopes[0], scopes);
        }

        final GoogleApiClient.Builder builder = new GoogleApiClient.Builder(activity, this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gsoBuilder.build());

        return builder.build();
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void login(Activity activity) {
        SocialNetworkManager.getInstance().setSocialNetworkID(ID);
        connectAndRequestGoogleAccount(RC_SIGN_IN, REQUEST_RESOLVE_ERROR);
    }

    @Override
    public boolean isLoggedIn() {
        String token = SocialNetworkManager.getInstance().getSharedPreferces().getString(SocialNetworkManager.AUTH_TOKEN, null);
        return token != null;
    }

    @Override
    public void logout() {
        try {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (!status.isSuccess()) {
                        Log.w(TAG, "Couldn't clear account and credentials");
                    } else {
                        SocialNetworkManager.getInstance().getSharedPreferces()
                                .edit()
                                .putInt(SocialNetworkManager.AUTH_ID, 0)
                                .putString(SocialNetworkManager.AUTH_TOKEN, null)
                                .apply();
                    }
                }
            });
        } catch (IllegalStateException e) {
            Log.e(TAG, "Failed to clear the Google Plus Session", e);
        }
    }

    @Override
    public void sharePost(Activity activity) {

    }

    private void connectAndRequestGoogleAccount(int signInRequestCode, int errorResolutionRequestCode) {
        if (googleApiClient.isConnected()) {
            requestGoogleAccount(signInRequestCode);
        } else if (!googleApiClient.isConnecting()) {
            googleApiClient.connect();
        }
    }

    private void requestGoogleAccount(int signInRequestCode) {
        final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(signInIntent, signInRequestCode);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestGoogleAccount(RC_SIGN_IN);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (resolvingError) {
            // Already attempting to resolve an error.
            return;
        }
        if (connectionResult.hasResolution()) {
            Log.v(TAG, "Connection failed. Trying to start the resolution.");
            try {
                resolvingError = true;
                connectionResult.startResolutionForResult(activity, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                googleApiClient.connect();
            }
        } else {
            Log.v(TAG, "Connection failed. No resolution was possible.");
            callback.onError(new AuthError(connectionResult.getErrorMessage(), connectionResult.getErrorCode()));
            resolvingError = true;
        }
    }
}
