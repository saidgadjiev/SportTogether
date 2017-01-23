package ru.mail.sporttogether.activities.presenter

import ru.mail.sporttogether.activities.view.EditEventView
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.Event
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by bagrusss on 22.01.17
 */
class EditEventActivityPresenterImp(private var view: EditEventView?) : EditEventActivityPresenter {

    @Inject lateinit var eventManager: EventsManager
    @Inject lateinit var eventApi: EventsAPI

    private var apiSubscription: Subscription? = null

    init {
        App.injector.usePresenterComponent().inject(this)
    }

    override fun updateEvent(e: Event) {
        apiSubscription?.unsubscribe()
        apiSubscription = eventApi.updateEvent(e.id, e)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view?.onError()
                    }

                    override fun onNext(t: Response<Any>) {
                        if (t.code == 0) {
                            eventManager.updateEvent(e)
                            view?.eventEdited()
                        } else {
                            view?.onError()
                        }
                    }

                })
    }


    override fun cancelEvent(e: Event) {
        apiSubscription?.unsubscribe()
        apiSubscription = eventApi.cancelEvent(e.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Any>>() {

                    override fun onNext(t: Response<Any>) {
                        if (t.code == 0) {
                            view?.eventCancelled()
                            eventManager.deleteEvent(e)
                        } else view?.onError()
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        view?.onError()
                    }

                })

    }

    override fun onDestroy() {
        super.onDestroy()
        apiSubscription?.unsubscribe()
    }

}