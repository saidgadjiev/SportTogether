package ru.mail.sporttogether.dagger.subcomponents

import dagger.Subcomponent
import ru.mail.sporttogether.fcm.CustomFirebaseMessagingService

/**
 * Created by bagrusss on 25.11.16.
 *
 */

@Subcomponent
interface ServiceComponent {
    fun inject(service: CustomFirebaseMessagingService)
}