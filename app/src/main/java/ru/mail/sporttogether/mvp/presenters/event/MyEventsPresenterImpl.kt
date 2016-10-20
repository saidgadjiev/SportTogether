package ru.mail.sporttogether.mvp.presenters.event

import android.util.Log
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.mvp.presenters.IPresenter
import ru.mail.sporttogether.mvp.views.event.IListEventView
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.EventsResponse
import ru.mail.sporttogether.net.responses.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Created by bagrusss on 15.10.16.
 *
 */
class MyEventsPresenterImpl : MyEventsPresenter, IPresenter {
    @Inject lateinit var api: EventsAPI

    private val view: IListEventView

    constructor(view: IListEventView) {
        this.view = view
        App.injector
                .usePresenterComponent()
                .inject(this)
    }

    override fun getMyEvents() {
        api.getMyEvents()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(object : Subscriber<Response<EventsResponse>>() {
            override fun onNext(response: Response<EventsResponse>) {
                val testEvents: ArrayList<Event> = ArrayList()
//                val event1 = Event("name_event1", 1, 54.894354, 34.5959595959, 1, 1, 20, "descr_1", false, 1479822400, 0)
//                val event2 = Event("name_event2", 2, 37.00, 47.299998, 1, 1, 350, "descr_2", true, 1478522400, 0)
//                val event3 = Event("name_event3", 2, 1.00, 17.28, 1, 1, 350, "descr_3", false, 1478822400, 0)
//                testEvents.add(event1)
//                testEvents.add(event2)
//                testEvents.add(event3)
                view.loadEvents(response.data)
            }

            override fun onError(e: Throwable) {
                Log.e("#MY ", e.message)
            }

            override fun onCompleted() {

            }

        })
    }
}