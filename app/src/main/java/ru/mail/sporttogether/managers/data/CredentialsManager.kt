package ru.mail.sporttogether.managers.data

import com.auth0.android.result.Credentials
import ru.mail.sporttogether.net.models.User

/**
 * Created by bagrusss on 14.10.16.
 */
interface CredentialsManager {
    fun saveCredentials(credentials: Credentials)
    fun getCredentials(): Credentials
    fun deleteCredentials()
    fun saveUserData(user: User)
    fun getUserData(): User
}