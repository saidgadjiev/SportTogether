package ru.mail.sporttogether.managers.headers

/**
 * Created by bagrusss on 14.10.16.
 *
 */
interface IHeaderManager {
    fun getHeaders(): Map<String, String>
}