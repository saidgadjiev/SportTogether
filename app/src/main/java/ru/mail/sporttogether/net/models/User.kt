package ru.mail.sporttogether.net.models

/**
 * Created by bagrusss on 30.09.16.
 *
 */
data class User(
        val clientId: String,
        val id: Long,
        val role: Int,
        val name: String?,
        val avatar: String?
) {
    constructor(clientId: String, id: Long, role: Int) : this(clientId, id, role, null, null)
}
