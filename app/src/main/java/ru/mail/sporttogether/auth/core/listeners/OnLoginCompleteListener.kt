package ru.mail.sporttogether.auth.core.listeners


import ru.mail.sporttogether.auth.core.SocialNetworkError

/**
 * Created by said on 13.11.16.
 */

interface OnLoginCompleteListener {
    fun onSuccess(ID: Int)

    fun onCancel()

    fun onError(socialNetworkError: SocialNetworkError)
}
