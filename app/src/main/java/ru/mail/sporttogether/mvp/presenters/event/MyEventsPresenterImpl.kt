package ru.mail.sporttogether.mvp.presenters.event

import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.mvp.views.event.IListEventView
import ru.mail.sporttogether.net.api.EventsAPI
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
class MyEventsPresenterImpl : MyEventsPresenter {
    @Inject lateinit var api: EventsAPI

    private val view: IListEventView

    constructor(view: IListEventView) {
        this.view = view
        App.injector
                .usePresenterComponent()
                .inject(this)
    }

    fun getMyEvents() {
        api.getMyEvents()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(object : Subscriber<Response<EventsResponse>>() {
            override fun onNext(response: Response<EventsResponse>) {
                println(response.code)
                println(response.message)
                response.data?.forEach { e -> println(e.id) }
                val list = ArrayList<String>()
                list.add("1")
                list.add("2")
                list.add("3")
                view.loadEvents(list)
            }

            override fun onError(e: Throwable) {
            }

            override fun onCompleted() {

            }

        })
    }
}