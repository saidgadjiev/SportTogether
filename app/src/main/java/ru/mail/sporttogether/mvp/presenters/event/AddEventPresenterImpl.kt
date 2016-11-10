package ru.mail.sporttogether.mvp.presenters.event

import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.managers.events.EventsManager
import ru.mail.sporttogether.mvp.views.event.IAddEventView
import ru.mail.sporttogether.net.api.CategoriesAPI
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.responses.CategoriesResponse
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
class AddEventPresenterImpl(var view: IAddEventView?) : AddEventPresenter {

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

    override fun searchCategory(category: String) {

    }

    override fun addEventClicked(name: String,
                                 categoryId: Long,
                                 lat: Double,
                                 lng: Double,
                                 description: String) {
        val event = Event(
                name = name,
                categoryId = categoryId,
                lat = lat,
                lng = lng,
                description = description,
                maxPeople = 5,
                date = (System.currentTimeMillis()) + 1000 * 60 * 63) // сделаем выбор времени и даты позже

        eventSubscribtion = eventsApi.createEvent(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Event>>() {
                    override fun onCompleted() {

                    }

                    override fun onNext(response: Response<Event>) {
                        if (response.code == 0) {
                            eventsManager.addEvent(response.data)
                            view?.onEventAdded(response.data.name)
                        }
                    }

                    override fun onError(e: Throwable) {
                        view?.showAddError("Error!")
                    }

                })
    }

    override fun loadCategories() {
        categoriesSubscribtion = categoriesApi.getAllCategoryes() //TODO опечатка
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

    override fun loadCategoriesBySubname(subname: String) {
        categoriesSubscribtion = categoriesApi.getCategoriesBySubname(subname) //TODO опечатка
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<CategoriesResponse>>() {
                    override fun onError(e: Throwable) {
                        view?.showAddError("Error!")
                        view?.invisibleCategoryProgressBar()
                    }

                    override fun onNext(response: Response<CategoriesResponse>) {
                        view?.onCategoriesLoaded(response.data)
                        view?.invisibleCategoryProgressBar()
                    }

                    override fun onCompleted() {

                    }

                })
    }

}