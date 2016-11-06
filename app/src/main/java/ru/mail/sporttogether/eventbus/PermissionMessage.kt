package ru.mail.sporttogether.eventbus

/**
 * Created by bagrusss on 06.11.16.
 *
 */
data class PermissionMessage(
        val reqCode: Int,
        var permissionsList: List<String>
)