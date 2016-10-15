package ru.mail.sporttogether.managers.headers

import ru.mail.sporttogether.managers.LocationManager
import java.util.*

/**
 * Created by bagrusss on 08.10.16.
 *
 */

class HeaderManagerImpl(locationManager: LocationManager) : IHeaderManager {

    var token: String? = null
    var clientId: String? = null
    val headers = TreeMap<String, String>()

    override fun getHeaders(): Map<String, String> {
        headers.clear()
        token?.let {
            headers.put(KEY_TOKEN, it)
        }
        clientId?.let {
            headers.put(KEY_CLIENT_ID, it)
        }
        return headers
    }

    companion object {
        @JvmStatic val KEY_TOKEN = "Token"
        @JvmStatic val KEY_CLIENT_ID = "ClientId"
    }
}
