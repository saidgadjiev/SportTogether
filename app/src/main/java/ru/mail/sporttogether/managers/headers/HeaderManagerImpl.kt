package ru.mail.sporttogether.managers.headers

import java.util.*

/**
 * Created by bagrusss on 08.10.16.
 *
 */

class HeaderManagerImpl() : IHeaderManager {

    var token: String = ""
    var clientId: String = ""
    val headers = TreeMap<String, String>()

    override fun getHeaders(): Map<String, String> {
        headers.clear()
        headers.put(KEY_TOKEN, token)
        headers.put(KEY_CLIENT_ID, clientId)
        return headers
    }

    companion object {
        @JvmStatic val KEY_TOKEN = "Token"
        @JvmStatic val KEY_CLIENT_ID = "ClientId"
    }
}
