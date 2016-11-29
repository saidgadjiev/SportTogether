package ru.mail.sporttogether.managers.data

import android.content.Context

/**
 * Created by said on 20.10.16.
 */
interface ITokenManager {
    fun saveToken(context: Context, token: String?)
    fun getToken(context: Context): String
    fun deleteToken(context: Context)
}