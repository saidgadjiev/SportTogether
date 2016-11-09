package ru.mail.sporttogether.mvp.presenters.event

import android.util.Log
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.mvp.presenters.IPresenter
import ru.mail.sporttogether.mvp.views.event.IListEventView
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.responses.EventsResponse
import ru.mail.sporttogether.net.responses.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
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