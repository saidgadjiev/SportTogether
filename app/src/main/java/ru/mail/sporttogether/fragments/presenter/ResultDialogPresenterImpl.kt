package ru.mail.sporttogether.fragments.presenter

import ru.mail.sporttogether.R
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.fragments.view.ResultView
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.EventResult
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by bagrusss on 22.01.17
 */
class ResultDialogPresenterImpl(private var view: ResultView?) : ResultDialogPresenter {

    @Inject lateinit var eventsApi: EventsAPI
    @Inject lateinit var eventManager: EventsManager

    private var subscription: Subscription? = null

    init {
        App.injector.usePresenterComponent().inject(this)
    }

    override fun sendResult(result: String, event: Event) {
        val eventResult = EventResult(id = event.id, result = result)
        view?.showProgressDialog()
        subscription = eventsApi.updateResult(eventResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onError(e: Throwable) {
                        view?.let {
                            it.hideProgressDialog()
                            it.showToast(R.string.error)
                        }
                    }

                    override fun onNext(t: Response<Any>) {
                        if (t.code == 0) {
                            event.result = result
                            eventManager.resultEvent(event)
                            view?.let {
                                it.hideProgressDialog()
                                it.onResultSent()
                            }
                        } else {
                            view?.showToast(R.string.error)
                        }

                    }

                    override fun onCompleted() {

                    }
                })
    }


    override fun onDestroy() {
        view = null
    }
}