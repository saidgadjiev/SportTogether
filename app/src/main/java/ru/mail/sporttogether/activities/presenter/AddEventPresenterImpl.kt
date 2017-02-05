package ru.mail.sporttogether.activities.presenter

import android.os.Bundle
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import ru.mail.sporttogether.activities.view.AddEventView
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.net.CategoriesResponse
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.api.CategoriesAPI
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.api.YandexMapsApi
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.EventResult
import ru.mail.sporttogether.net.models.yandex.maps.GeoObject
import ru.mail.sporttogether.utils.DateUtils
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Created by bagrusss on 15.10.16.
 *
 */
class AddEventPresenterImpl(var view: AddEventView?) : AddEventPresenter {

    @Inject lateinit var eventsApi: EventsAPI
    @Inject lateinit var categoriesApi: CategoriesAPI
    @Inject lateinit var eventsManager: EventsManager
    @Inject lateinit var yandexApi: YandexMapsApi

    private var eventSubscribtion: Subscription? = null
    private var categoriesSubscribtion: Subscription? = null

    init {
        App.injector.usePresenterComponent().inject(this)
    }


    override fun onCreate(args: Bundle?, event: Event) {
        super.onCreate(args, event)
        yandexApi.getAddressByCoordinates(longlat = "" + event.lng + ',' + event.lat)
                .retry(10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<ArrayList<GeoObject>>() {
                    override fun onError(e: Throwable) {

                    }

                    override fun onCompleted() {
                    }

                    override fun onNext(t: ArrayList<GeoObject>) {
                        if (t.isNotEmpty()) {
                            view?.updateAddress(t[0].textAddress)
                            event.address = t[0].textAddress
                        }
                    }

                })

    }

    override fun onDestroy() {
        view = null
        eventSubscribtion?.unsubscribe()
        categoriesSubscribtion?.unsubscribe()
    }

    override fun searchCategory(category: String) {

    }

    override fun addEventClicked(event: Event, addMeNow: Boolean) {
        val sb = StringBuilder(event.category.name)
        sb.append(", ")
                .append(DateUtils.toXLongDateString(Date(event.date)))
                .append(", ")
                .append(event.maxPeople)
                .append(" чел.")
        val nameEvent = sb.toString()
        Log.d("#MY ", "generated name : " + nameEvent)
        event.name = nameEvent

        eventSubscribtion = eventsApi.createEvent(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Event>>() {
                    override fun onCompleted() {

                    }

                    override fun onNext(response: Response<Event>) {
                        if (response.code == 0) {
                            eventsManager.addEvent(response.data)
                            if (addMeNow == true) {
                                ++event.nowPeople
                                join(response.data.id)
                            }
                            val data = response.data
                            event.id = data.id
                            event.user = data.user
                            view?.onEventAdded(event)
                        } else {
                            view?.showAddError(response.message)
                        }
                    }

                    override fun onError(e: Throwable) {
                        view?.showAddError("Не удалось соединиться с сервером")
                    }

                })
    }



    override fun sendResult(id: Long, result: String) {
        eventSubscribtion?.unsubscribe()
        val update = EventResult(id, result)
        eventSubscribtion = eventsApi.updateResult(update)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onNext(t: Response<Any>) {
                        if (t.code == 0) {
                            view?.resultSended()
                        }
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view?.showToast(e.message!!)
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

    fun join(id: Long) {
        eventsApi.joinToEvent(id, FirebaseInstanceId.getInstance().token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onNext(t: Response<Any>) {
                        if (t.code === 0) {
                            Log.i("#MY " + javaClass.simpleName, "Вы присоеденены к событию")
                        } else view?.showToast(t.message)
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {

                    }

                })
    }

}