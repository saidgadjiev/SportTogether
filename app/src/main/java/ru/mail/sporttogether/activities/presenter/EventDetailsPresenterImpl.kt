package ru.mail.sporttogether.activities.presenter

import ru.mail.sporttogether.activities.view.EventDetailsView
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.net.api.YandexMapsApi
import ru.mail.sporttogether.net.models.yandex.maps.GeoObject
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions
import javax.inject.Inject

/**
 * Created by bagrusss on 12.02.17
 */
class EventDetailsPresenterImpl(var view: EventDetailsView?) : EventDetailsPresenter {

    @Inject lateinit var yandexApi: YandexMapsApi

    private var subscription = Subscriptions.empty()

    init {
        App.injector.usePresenterComponent().inject(this)
    }

    override fun loadAddress(lat: Double, lng: Double) {
        subscription = yandexApi.getAddressByCoordinates(longlat = "$lng,$lat")
                .retry(10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<ArrayList<GeoObject>>() {
                    override fun onError(e: Throwable) {

                    }

                    override fun onNext(t: ArrayList<GeoObject>) {
                        if (t.isNotEmpty()) {
                            view?.updateAddress(t[0].textAddress)
                        }
                    }

                    override fun onCompleted() {
                    }

                })

    }

    override fun onDestroy() {
        subscription.unsubscribe()
        view = null
        super.onDestroy()
    }

}