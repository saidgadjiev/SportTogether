package ru.mail.sporttogether.managers.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.auth0.android.result.Credentials;

import ru.mail.sporttogether.R;
import ru.mail.sporttogether.net.utils.Constants;

/**
 * Created by said on 08.10.16.
 */

public class CredentialsManagerImpl implements ICredentialsManager {

    @Override
    public void saveCredentials(@NonNull Context context, @NonNull Credentials credentials) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.auth0_preferences), Context.MODE_PRIVATE);

        sharedPref.edit()
                .putString(Constants.ID_TOKEN, credentials.getIdToken())
                .putString(Constants.REFRESH_TOKEN, credentials.getRefreshToken())
                .putString(Constants.ACCESS_TOKEN, credentials.getAccessToken())
                .putString(Constants.CREDENTIAL_TYPE, credentials.getType())
                .apply();
    }

    @NonNull
    @Override
    public Credentials getCredentials(@NonNull Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.auth0_preferences), Context.MODE_PRIVATE);

        Credentials credentials = new Credentials(
                sharedPref.getString(Constants.ID_TOKEN, null),
                sharedPref.getString(Constants.ACCESS_TOKEN, null),
                sharedPref.getString(Constants.CREDENTIAL_TYPE, null),
                sharedPref.getString(Constants.REFRESH_TOKEN, null));

        return credentials;
    }

    @Override
    public void deleteCredentials(@NonNull Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.auth0_preferences), Context.MODE_PRIVATE);

        sharedPref.edit()
                .putString(Constants.ID_TOKEN, null)
                .putString(Constants.REFRESH_TOKEN, null)
                .putString(Constants.ACCESS_TOKEN, null)
                .putString(Constants.CREDENTIAL_TYPE, null)
                .apply();
    }
}
