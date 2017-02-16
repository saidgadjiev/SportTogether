package ru.mail.sporttogether.fragments.presenter

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.fragments.view.FillEventView
import ru.mail.sporttogether.net.CategoriesResponse
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.api.CategoriesAPI
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions
import javax.inject.Inject

/**
 * Created by bagrusss on 14.02.17
 */
class FillEventPresenterImpl(var view: FillEventView?) : FillEventPresenter() {

    @Inject lateinit var categoriesApi: CategoriesAPI


    private var categoriesSubscribtion = Subscriptions.empty()


    init {
        App.injector.usePresenterComponent().inject(this)
    }


    override fun onMapReady(mapa: GoogleMap) {
        super.onMapReady(mapa)
        mapa.uiSettings.setAllGesturesEnabled(false)
        mapa.uiSettings.isMapToolbarEnabled = false
        mapa.setOnMapClickListener {

        }
        val event = eventsManager.getCreatingEvent()
        event?.let {
            val latlng = LatLng(it.lat, it.lng)
            mapa.moveCamera(CameraUpdateFactory.newLatLng(latlng))
            mapa.addMarker(MarkerOptions().position(latlng))
            view?.updateAddress(it.address)
        }
    }

    override fun loadCategoriesBySubname(subname: String) {
        categoriesSubscribtion.unsubscribe()
        categoriesSubscribtion = categoriesApi.getCategoriesBySubname(subname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<CategoriesResponse>>() {
                    override fun onError(e: Throwable) {
                        view?.showToast("Error!")
                    }

                    override fun onNext(response: Response<CategoriesResponse>) {
                        view?.onCategoriesLoaded(response.data)
                    }

                    override fun onCompleted() {

                    }

                })
    }

    override fun fillEvent(name: String,
                           sport: String,
                           maxPeople: Int,
                           description: String,
                           addressText:String,
                           time: Long,
                           joinToEvent: Boolean,
                           needAddTemplate: Boolean) {
        val event = eventsManager.getCreatingEvent()
        event?.let {
            it.name = name
            it.category.name = sport
            it.maxPeople = maxPeople
            it.description = description
            it.date = time
            eventsManager.setNeedJoin(joinToEvent)
            it.id = if (needAddTemplate) -1 else -2 //нужно для создания события на 3 этапе
        }
        view?.nextStep()
    }

    override fun onDestroy() {
        view = null
        categoriesSubscribtion.unsubscribe()
        super.onDestroy()
    }


}