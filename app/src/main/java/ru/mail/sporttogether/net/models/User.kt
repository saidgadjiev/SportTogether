package ru.mail.sporttogether.net.models

/**
 * Created by bagrusss on 30.09.16
 */
data class User(
        val clientId: String = "",
        var id: Long = 0,
        var role: Int = 0,
        var name: String = "",
        var avatar: String = ""
)
