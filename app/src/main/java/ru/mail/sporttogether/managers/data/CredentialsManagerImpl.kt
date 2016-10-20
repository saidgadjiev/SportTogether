package ru.mail.sporttogether.managers.data;

import android.content.Context
import com.auth0.android.result.Credentials
import ru.mail.sporttogether.R
import ru.mail.sporttogether.net.utils.Constants

/**
 * Created by said on 08.10.16.
 */

class CredentialsManagerImpl: ICredentialsManager {

    override fun saveCredentials(context: Context, credentials: Credentials?) {
        val sharedPref = context.getSharedPreferences(
                context.getString(R.string.auth0_preferences), Context.MODE_PRIVATE)

        sharedPref.edit()
                .putString(Constants.ID_TOKEN, credentials?.getIdToken())
                .putString(Constants.REFRESH_TOKEN, credentials?.getRefreshToken())
                .putString(Constants.ACCESS_TOKEN, credentials?.getAccessToken())
                .putString(Constants.CREDENTIAL_TYPE, credentials?.getType())
                .apply()
    }

    override fun deleteCredentials(context: Context) {
        val sharedPref = context.getSharedPreferences(
                context.getString(R.string.auth0_preferences), Context.MODE_PRIVATE)

        sharedPref.edit()
                .putString(Constants.ID_TOKEN, null)
                .putString(Constants.REFRESH_TOKEN, null)
                .putString(Constants.ACCESS_TOKEN, null)
                .putString(Constants.CREDENTIAL_TYPE, null)
                .apply()
    }

    override fun getCredentials(context: Context): Credentials {
        val sharedPref = context.getSharedPreferences(
                context.getString(R.string.auth0_preferences), Context.MODE_PRIVATE)

        val credentials = Credentials(
                sharedPref.getString(Constants.ID_TOKEN, null),
                sharedPref.getString(Constants.ACCESS_TOKEN, null),
                sharedPref.getString(Constants.CREDENTIAL_TYPE, null),
                sharedPref.getString(Constants.REFRESH_TOKEN, null))

        return credentials
    }
}
