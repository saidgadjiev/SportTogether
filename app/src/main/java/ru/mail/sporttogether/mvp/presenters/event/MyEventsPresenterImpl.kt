package ru.mail.sporttogether.mvp.presenters.event

import android.util.Log
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.managers.data.CredentialsManager
import ru.mail.sporttogether.mvp.views.event.IMyEventsView
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.User
import ru.mail.sporttogether.net.responses.EventsResponse
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by bagrusss on 15.10.16.
 *
 */
class MyEventsPresenterImpl(private var view: IMyEventsView?) : MyEventsPresenter {

    @Inject lateinit var eventsApi: EventsAPI
    @Inject lateinit var credentialsManager: CredentialsManager
    private val user: User

    init {
        App.injector
                .usePresenterComponent()
                .inject(this)
        user = credentialsManager.getUserData()
    }

    override fun getMyEvents() {
        var myEvents: EventsResponse? = null
        eventsApi.getAllEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMap { response ->
                    Observable.from(response.data)
                }
                .filter { //события которые создал пользователь
                    it.userId == user.id
                }
                .toList()
                .subscribeOn(Schedulers.io())
                .flatMap { list ->
                    view?.clearEvents()
                    if (list.size > 0) {
                        list.add(0, Event(id = -1, name = "Организованные"))
                        view?.addOrganizedEvents(list)
                    }
                    eventsApi.getMyEvents()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMap { response ->
                    myEvents = response.data
                    Observable.from(myEvents)
                }
                .filter { item -> //события которые завершились
                    item.isEnded == true
                }
                .toList()
                .flatMap { list ->
                    if (list.size > 0) {
                        list.add(0, Event(id = -1, name = "Завершились"))
                        view?.addEndedEvents(list)
                    }
                    Observable.from(myEvents)
                }
                .filter { item -> //события которые не завершились
                    item.isEnded == false
                }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<MutableList<Event>>() {
                    override fun onNext(list: MutableList<Event>) {
                        if (list.size > 0) {
                            list.add(0, Event(id = -1, name = "Подписки"))
                            view?.addMyEvents(list)
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.e("#MY ", e.message, e)
                    }

                    override fun onCompleted() {

                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        view = null
    }
}