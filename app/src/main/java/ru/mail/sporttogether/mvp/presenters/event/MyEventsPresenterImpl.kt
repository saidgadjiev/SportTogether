package ru.mail.sporttogether.mvp.presenters.event

import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.mvp.views.event.IMyEventsView
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.User
import ru.mail.sporttogether.net.responses.EventsResponse
import ru.mail.sporttogether.net.responses.Response
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by bagrusss on 15.10.16
 */
class MyEventsPresenterImpl(private var view: IMyEventsView?) : MyEventsPresenter {

    @Inject lateinit var eventsApi: EventsAPI
    private var socialNetworkManager: SocialNetworkManager
    private val user: User

    private var eventsSubscription: Subscription? = null

    init {
        App.injector
                .usePresenterComponent()
                .inject(this)

        socialNetworkManager = SocialNetworkManager.instance
        user = socialNetworkManager.activeUser
    }

    override fun getMyEvents() {
        eventsSubscription = eventsApi.getMyEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<EventsResponse>>() {
                    override fun onError(e: Throwable) {
                        view?.showToast("Не удалось загрузить список моиз событий")
                    }

                    override fun onNext(t: Response<EventsResponse>) {
                        if (t.code == 0) {
                            view?.updateEvents(t.data)
                        }
                    }

                    override fun onCompleted() {

                    }

                })
    }

    override fun onEventClicked(e: Event) {
        if (e.user.id === user.id)
            view?.openEditActivity(e.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        view = null
        eventsSubscription?.unsubscribe()
    }
}