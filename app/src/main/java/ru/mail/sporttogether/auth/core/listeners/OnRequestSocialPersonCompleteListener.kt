package ru.mail.sporttogether.auth.core.listeners

import ru.mail.sporttogether.auth.core.SocialNetworkError
import ru.mail.sporttogether.auth.core.persons.SocialPerson

/**
 * Created by said on 21.11.16.
 */
interface OnRequestSocialPersonCompleteListener {
    fun onError(socialNetworkError: SocialNetworkError)

    fun onComplete(person: SocialPerson, ID: Int)
}
