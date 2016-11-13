package ru.mail.sporttogether.managers.data

import android.content.Context
import android.content.SharedPreferences
import com.auth0.android.result.Credentials
import ru.mail.sporttogether.R
import ru.mail.sporttogether.net.models.User

/**
 * Created by said on 08.10.16.
 *
 */

class CredentialsManagerImpl(val context: Context) : CredentialsManager {

    val sharedPref: SharedPreferences

    init {
        sharedPref = context.getSharedPreferences(
                context.getString(R.string.auth0_preferences), Context.MODE_PRIVATE)
    }

    override fun saveUserData(user: User) {

        sharedPref.edit()
                .putLong(USER_ID_KEY, user.id)
                .putString(CLIENT_ID_KEY, user.clientId)
                .apply()
    }

    override fun saveCredentials(credentials: Credentials) {
        sharedPref.edit()
                .putString(ID_TOKEN, credentials.idToken)
                .putString(REFRESH_TOKEN, credentials.refreshToken)
                .putString(ACCESS_TOKEN, credentials.accessToken)
                .putString(CREDENTIAL_TYPE, credentials.type)
                .apply()
    }

    override fun getUserData(): User = User(
            clientId = sharedPref.getString(CLIENT_ID_KEY, ""),
            id = sharedPref.getLong(USER_ID_KEY, 0L),
            role = 1
    )

    override fun deleteCredentials() {

        sharedPref.edit()
                .putString(ID_TOKEN, null)
                .putString(REFRESH_TOKEN, null)
                .putString(ACCESS_TOKEN, null)
                .putString(CREDENTIAL_TYPE, null)
                .apply()
    }

    override fun getCredentials() = Credentials(
            sharedPref.getString(ID_TOKEN, null),
            sharedPref.getString(ACCESS_TOKEN, null),
            sharedPref.getString(CREDENTIAL_TYPE, null),
            sharedPref.getString(REFRESH_TOKEN, null))


    companion object {
        @JvmStatic val REFRESH_TOKEN: String = "AUTH0_REFRESH_TOKEN"
        @JvmStatic val ACCESS_TOKEN: String = "AUTH0_ACCESS_TOKEN"
        @JvmStatic val ID_TOKEN: String = "AUTH0_ID_TOKEN"
        @JvmStatic val CREDENTIAL_TYPE: String = "AUTH0_CREDENTIAL_TYPE"

        @JvmStatic val USER_ID_KEY: String = "USER_ID"
        @JvmStatic val CLIENT_ID_KEY: String = "USER_TOKEN"
    }
}
