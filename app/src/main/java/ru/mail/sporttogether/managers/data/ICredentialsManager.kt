package ru.mail.sporttogether.managers.data

import android.content.Context
import com.auth0.android.result.Credentials

/**
 * Created by bagrusss on 14.10.16.
 */
interface ICredentialsManager {
    fun saveCredentials(context: Context, credentials: Credentials?)
    fun getCredentials(context: Context): Credentials
    fun deleteCredentials(context: Context)
}