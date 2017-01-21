package ru.mail.sporttogether.dagger.subcomponents

import dagger.Subcomponent
import ru.mail.sporttogether.fcm.CustomFirebaseMessagingService
import ru.mail.sporttogether.services.UnjoinIntentService

/**
 * Created by bagrusss on 25.11.16.
 *
 */

@Subcomponent
interface ServiceComponent {
    fun inject(service: CustomFirebaseMessagingService)
    fun inject(service: UnjoinIntentService)
}