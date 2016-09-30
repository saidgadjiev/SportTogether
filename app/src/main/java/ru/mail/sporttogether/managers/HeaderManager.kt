package ru.mail.sporttogether.managers

import android.util.ArrayMap

/**
 * Created by bagrusss on 29.09.16.
 *
 */
class HeaderManager {

    var token: String? = null
    private var device: String? = null
    private var os: String? = null

    fun getHeaders(): ArrayMap<String, String> {
        val headers = ArrayMap<String, String>(3)
        headers.put(DEVICE_HEADER, device)
        headers.put(OS_HEADER, os)
        token?.let {
            headers.put(TOKEN_HEADER, it)
        }
        return headers
    }

    companion object {
        val DEVICE_HEADER = "device"
        val OS_HEADER = "os"
        val TOKEN_HEADER = "cookie"
    }

}