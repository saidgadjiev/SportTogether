package ru.mail.sporttogether.net.responses

/**
 * Created by bagrusss on 29.09.16.
 *
 */
data class Response<out T>(
        val code: Int,
        val message: String,
        val data: T
)