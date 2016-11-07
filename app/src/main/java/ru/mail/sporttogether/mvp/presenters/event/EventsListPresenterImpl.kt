package ru.mail.sporttogether.mvp.presenters.event

import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.managers.events.EventsManager
import ru.mail.sporttogether.mvp.views.event.IListEventView
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.Event
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by bagrusss on 15.10.16.
 */
class EventsListPresenterImpl : EventsListPresenter {
    @Inject lateinit var api: EventsAPI
    @Inject lateinit var eventsManager: EventsManager

    private var view: IListEventView? = null

    constructor(view: IListEventView) {
        this.view = view
        App.injector
                .usePresenterComponent()
                .inject(this)
    }

    private var eventSubscribtion: Subscription? = null

    override fun onStart() {
        super.onStart()
        loadEvents()
        eventSubscribtion = eventsManager.getObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<EventsManager.NewData<*>>() {
                    override fun onNext(t: EventsManager.NewData<*>) {
                        when (t.type) {
                            EventsManager.UpdateType.ADD -> {
                                view?.addEvent(t.data as Event)
                            }

                            EventsManager.UpdateType.NEW_LIST -> {
                                view?.loadEvents(t.data as MutableList<Event>)
                            }

                            else -> {

                            }
                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onCompleted() {

                    }
                })
    }

    override fun onStop() {
        super.onStop()
    }

    override fun loadEvents() {
        val events = eventsManager.getEvents()
        if (events.size > 0) {
            view?.loadEvents(events)
        }
        /*api.getAllEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<EventsResponse>>() {
                    override fun onNext(response: Response<EventsResponse>) {
                        println(response.code)
                        println(response.message)
                        view.loadEvents(response.data)
                    }

                    override fun onError(e: Throwable) {
                        Log.e("#MY ", e.message)
                    }

                    override fun onCompleted() {

                    }
                })*/
    }

    override fun onDestroy() {
        view = null
        eventSubscribtion?.unsubscribe()
        super.onDestroy()
    }
}