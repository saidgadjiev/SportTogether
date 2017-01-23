package ru.mail.sporttogether.fragments.presenter

import android.os.Bundle
import android.support.annotation.CallSuper
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.fragments.view.EventsListView
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.mvp.IPresenter
import ru.mail.sporttogether.net.EventsResponse
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.Event
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by bagrusss on 15.10.16
 */
abstract class AbstractEventsListPresenter(private var view: EventsListView?) : IPresenter {

    @Inject lateinit var eventsApi: EventsAPI
    @Inject lateinit var eventsManager: EventsManager

    protected var apiSubscription: Subscription? = null
    protected var eventsSubscription: Subscription? = null


    init {
        App.injector
                .usePresenterComponent()
                .inject(this)
    }

    abstract fun getApiObservable(): Observable<Response<EventsResponse>>

    override fun onCreate(args: Bundle?) {
        super.onCreate(args)
        eventsSubscription = eventsManager.getObservable().subscribe { data ->
            onEventChanges(data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        view = null
        apiSubscription?.unsubscribe()
        eventsSubscription?.unsubscribe()
    }

    fun getEvents() {
        view?.showProgressDialog()
        //TODO задержка костыль чтобы не было вспышки при быстром интернете
        apiSubscription = getApiObservable().delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<EventsResponse>>() {
                    override fun onError(e: Throwable) {
                        view?.let {
                            it.hideProgressDialog()
                            it.showToast("Не удалось загрузить список событий")
                        }
                    }

                    override fun onNext(t: Response<EventsResponse>) {
                        if (t.code == 0) {
                            view?.swapEvents(t.data)
                        }
                        view?.hideProgressDialog()
                    }

                    override fun onCompleted() {

                    }

                })
    }

    @CallSuper
    protected open fun onEventChanges(newData: EventsManager.NewData<*>) {
        if (newData.type == EventsManager.UpdateType.DELETED) {
            view?.deleteEvent(newData.data as Event)
        }

        if (newData.type == EventsManager.UpdateType.UPDATE) {
            view?.updateEvent(newData.data as Event)
        }
    }


}