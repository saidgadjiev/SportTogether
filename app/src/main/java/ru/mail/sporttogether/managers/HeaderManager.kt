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
        headers.put("Token", token)
        headers.put("ClientId", clientId)
    }

    fun getHeaders(): Map<String, String> {

        return headers
    }
}
