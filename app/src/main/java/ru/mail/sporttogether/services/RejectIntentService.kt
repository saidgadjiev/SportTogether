package ru.mail.sporttogether.services

import android.app.IntentService
import android.content.Intent
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.net.api.EventsAPI
import javax.inject.Inject

/**
 * Created by said on 21.10.16.
 */
class RejectIntentService : IntentService {

    @Inject lateinit var api: EventsAPI

    constructor(name: String?): super(name) {
        App.injector.useSharedComponent().inject(this)
    }

    override fun onHandleIntent(p0: Intent?) {
    }
}