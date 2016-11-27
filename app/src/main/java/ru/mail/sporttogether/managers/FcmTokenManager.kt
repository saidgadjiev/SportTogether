package ru.mail.sporttogether.managers.data;

import android.content.Context
import ru.mail.sporttogether.net.utils.Constants

/**
 * Created by said on 08.10.16.
 */

class FcmTokenManager: ITokenManager {

    override fun saveToken(context: Context, token: String?) {
        val sharedPref = context.getSharedPreferences(Constants.FCM_TOKEN, Context.MODE_PRIVATE)

        sharedPref.edit()
                .putString(Constants.FCM_TOKEN, token)
                .apply()
    }

    override fun deleteToken(context: Context) {
        val sharedPref = context.getSharedPreferences(Constants.FCM_TOKEN, Context.MODE_PRIVATE)

        sharedPref.edit()
                .putString(Constants.FCM_TOKEN, null)
                .apply()
    }

    override fun getToken(context: Context): String {
        val sharedPref = context.getSharedPreferences(Constants.FCM_TOKEN, Context.MODE_PRIVATE)

        val token = sharedPref.getString(Constants.FCM_TOKEN, null)

        return token
    }
}
