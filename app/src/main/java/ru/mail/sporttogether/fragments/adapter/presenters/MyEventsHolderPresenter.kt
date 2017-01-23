package ru.mail.sporttogether.fragments.adapter.presenters

import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.fragments.adapter.views.MyEventsView
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.Event
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by bagrusss on 21.01.17
 */
class MyEventsHolderPresenter(private var view: MyEventsView? = null) : TwoActionsHolderPresenter(view) {

    @Inject lateinit var eventApi: EventsAPI

    init {
        App.injector.useViewComponent().inject(this)
    }

    fun doAngry(event: Event) {
        eventApi.report(event.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onError(e: Throwable) {
                        unsubscribe()
                    }

                    override fun onCompleted() {

                    }

                    override fun onNext(t: Response<Any>) {
                        if (t.code == 0) {
                            eventsManager.angryEvent(event)
                            view?.eventAngried()
                        }
                        unsubscribe()
                    }

                })
    }
}