package ru.mail.sporttogether.mvp.presenters.event.lists

import ru.mail.sporttogether.mvp.presenters.IPresenter
import ru.mail.sporttogether.mvp.views.event.IEventListView
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.responses.EventsResponse
import ru.mail.sporttogether.net.responses.Response
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by bagrusss on 15.10.16
 */
abstract class EventsListPresenter(protected var view: IEventListView?) : IPresenter {

    @Inject lateinit var eventsApi: EventsAPI
    private var apiSubscription: Subscription? = null

    abstract fun getApiObservable(): Observable<Response<EventsResponse>>

    fun getEvents() {
        apiSubscription = getApiObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<EventsResponse>>() {
                    override fun onError(e: Throwable) {
                        view?.showToast("Не удалось загрузить список моиз событий")
                    }

                    override fun onNext(t: Response<EventsResponse>) {
                        if (t.code == 0) {
                            view?.swapEvents(t.data)
                        }
                    }

                    override fun onCompleted() {

                    }

                })
    }

    override fun onDestroy() {
        super.onDestroy()
        view = null
        apiSubscription?.unsubscribe()
    }

}