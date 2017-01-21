package ru.mail.sporttogether.dagger.subcomponents

import dagger.Subcomponent
import ru.mail.sporttogether.services.CustomFirebaseInstanceIdService

/**
 * Created by said on 20.10.16
 */
@Subcomponent
interface SharedSubComponent {
    fun inject(service: CustomFirebaseInstanceIdService)
//    fun inject(service: UnjoinIntentService)
//    fun inject(service: RejectIntentService)
}