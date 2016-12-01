package ru.mail.sporttogether.dagger.subcomponents

import dagger.Subcomponent
import ru.mail.sporttogether.services.CustomFirebaseInstanceIdService
import ru.mail.sporttogether.services.CustomFirebaseMessagingService
import ru.mail.sporttogether.services.OkIntentService
import ru.mail.sporttogether.services.RejectIntentService

/**
 * Created by said on 20.10.16.
 */
@Subcomponent
interface SharedSubComponent {
    fun inject(service: CustomFirebaseMessagingService)
    fun inject(service: CustomFirebaseInstanceIdService)
    fun inject(service: OkIntentService)
    fun inject(service: RejectIntentService)
}