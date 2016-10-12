package ru.mail.sporttogether.managers

import java.util.*

/**
 * Created by bagrusss on 08.10.16.
 *
 */

class HeaderManager(locationManager: LocationManager) {

    var token: String? = null //call from java headerManager.setToken(sometoken)
    var clientId: String? = null
    val headers = TreeMap<String, String?>()

    fun getHeaders(): Map<String, String?> {
        headers.clear()
        token?.let {
            headers.put(KEY_TOKEN, token)
        }
        clientId?.let {
            headers.put(KEY_CLIENT_ID, clientId)
        }
        return headers
    }

    companion object {
        @JvmStatic val KEY_TOKEN = "Token"
        @JvmStatic val KEY_CLIENT_ID = "ClientId"
    }
}
