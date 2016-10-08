package ru.mail.sporttogether.managers

import java.util.*

/**
 * Created by bagrusss on 08.10.16.
 *
 */

class HeaderManager {

    private val token: String? = null
    private val clientId: String? = null
    private val headers: MutableMap<String, String> = HashMap()

    fun addHeaders(token: String, clientId: String) {
        headers.put(KEY_TOKEN, token)
        headers.put(KEY_CLIENT_ID, clientId)
    }

    fun getHeaders(): Map<String, String> {

        return headers
    }

    companion object {
        @JvmStatic val KEY_TOKEN = "Token"
        @JvmStatic val KEY_CLIENT_ID = "ClientId"
    }
}
