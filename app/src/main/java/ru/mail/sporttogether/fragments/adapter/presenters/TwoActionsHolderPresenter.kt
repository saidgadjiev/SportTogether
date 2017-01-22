package ru.mail.sporttogether.fragments.adapter.presenters

import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.fragments.adapter.views.TwoActionView
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.mvp.IPresenter
import ru.mail.sporttogether.net.models.Event
import javax.inject.Inject

/**
 * Created by bagrusss on 21.01.17
 */
open class TwoActionsHolderPresenter(protected var view: TwoActionView?) : IPresenter {

    @Inject lateinit var eventsManager: EventsManager
    @Volatile var event: Event? = null

    init {
        App.injector
                .usePresenterComponent()
                .inject(this)
    }

    fun itemClicked(event: Event) {
        eventsManager.showEvent(event)
    }

    open fun onBind(e: Event) {
        event?.let {
            synchronized(it) {
                event = e
            }
            return
        }
        event = e
    }

    open fun action1Clicked() {

    }

    open fun action2Clicked() {

    }

}