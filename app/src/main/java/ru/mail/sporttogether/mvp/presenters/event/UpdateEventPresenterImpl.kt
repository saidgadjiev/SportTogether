package ru.mail.sporttogether.mvp.presenters.event

import android.util.Log
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.managers.events.EventsManager
import ru.mail.sporttogether.mvp.views.event.IUpdateEventView
import ru.mail.sporttogether.net.api.CategoriesAPI
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.EventResult
import ru.mail.sporttogether.net.responses.Response
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by bagrusss on 07.10.16.
 *
 */
class UpdateEventPresenterImpl(var view: IUpdateEventView?) : UpdateEventPresenter {
    @Inject lateinit var eventsApi: EventsAPI

    @Inject lateinit var categoriesApi: CategoriesAPI
    @Inject lateinit var eventsManager: EventsManager
    private var eventSubscribtion: Subscription? = null


    private var categoriesSubscribtion: Subscription? = null
    init {
        App.injector.usePresenterComponent().inject(this)
    }


    override fun onDestroy() {
        view = null
        eventSubscribtion?.unsubscribe()
        categoriesSubscribtion?.unsubscribe()
    }


    override fun updateResult(id: Long, result: String) {
        Log.d("#MY " + javaClass.simpleName, "update result id : " + id)
        Log.d("#MY " + javaClass.simpleName, "update result str : " + result)
        val updateResultObservable: Observable<Response<Any>> = eventsApi.updateResult(EventResult(id, result))
        updateResultObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onCompleted() {

                    }

                    override fun onNext(response: Response<Any>) {
                        if (response.code == 0) {
                            view?.onResultUpdated()
                        }
                    }

                    override fun onError(e: Throwable) {
                        view?.showToast("Ошибка " + e.message)
                    }

                })

    }

}
