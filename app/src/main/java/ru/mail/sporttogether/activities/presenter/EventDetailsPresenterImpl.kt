package ru.mail.sporttogether.activities.presenter

import ru.mail.sporttogether.activities.view.EventDetailsView

/**
 * Created by bagrusss on 12.02.17
 */
class EventDetailsPresenterImpl(var view: EventDetailsView?) : EventDetailsPresenter {

    override fun onDestroy() {
        view = null
        super.onDestroy()
    }

}