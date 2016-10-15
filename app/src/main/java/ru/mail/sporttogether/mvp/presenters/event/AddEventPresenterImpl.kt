package ru.mail.sporttogether.mvp.presenters.event

import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.mvp.views.event.IAddEventView
import ru.mail.sporttogether.net.api.CategoriesAPI
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.CategoriesResponse
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.responses.Response
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by bagrusss on 15.10.16.
 *
 */
class AddEventPresenterImpl : AddEventPresenter {

    private var view: IAddEventView? = null

    @Inject lateinit var eventsApi: EventsAPI
    @Inject lateinit var categoriesApi: CategoriesAPI

    private var eventSubscribtion: Subscription? = null
    private var categoriesSubscribtion: Subscription? = null

    constructor(view: IAddEventView) {
        this.view = view
        App.injector.usePresenterComponent().inject(this)
    }

    override fun onDestroy() {
        view = null
        eventSubscribtion?.unsubscribe()
        categoriesSubscribtion?.unsubscribe()
    }

    override fun searchCategory(category: String) {

    }

    override fun addEventClicked(name: String, categoryId: Long, lat: Double, lng: Double) {
        val event = Event(
                name = name,
                categoryId = categoryId,
                latitude = lat,
                longtitude = lng,
                description = "some",
                maxPeople = 0,
                isEnded = false,
                date = 0L)
        eventSubscribtion = eventsApi.createEvent(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Event>>() {
                    override fun onCompleted() {

                    }

                    override fun onNext(response: Response<Event>) {
                        view?.onEventAdded(response.data.name)
                    }

                    override fun onError(e: Throwable) {
                        view?.showAddError("Error!")
                    }

                })
    }

    override fun loadCategories() {
        categoriesSubscribtion = categoriesApi.getAllCategoryes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<CategoriesResponse>>() {
                    override fun onError(e: Throwable) {
                        view?.showAddError("Error!")
                    }

                    override fun onNext(response: Response<CategoriesResponse>) {
                        view?.onCategoriesReady(response.data)
                    }

                    override fun onCompleted() {

                    }

                })
    }

}