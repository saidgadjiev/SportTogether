package ru.mail.sporttogether.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.MapView
import ru.mail.sporttogether.R
import ru.mail.sporttogether.data.binding.FabListener
import ru.mail.sporttogether.databinding.ActivityMapBinding
import ru.mail.sporttogether.mvp.presenters.map.IMapPresenter
import ru.mail.sporttogether.mvp.presenters.map.MapPresenterImpl
import ru.mail.sporttogether.mvp.views.map.IMapView
import ru.mail.sporttogether.net.responses.Response
import ru.mail.sporttogether.net.utils.RetrofitFactory
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MapActivity : AbstractActivity(), IMapView, FabListener {

    private lateinit var mapView: MapView
    private lateinit var binding: ActivityMapBinding

    private lateinit var presenter: IMapPresenter

    private val api = RetrofitFactory.API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        mapView = binding.mapview
        mapView.onCreate(savedInstanceState)
        presenter = MapPresenterImpl(this)
        mapView.getMapAsync(presenter)

        api.helloWorld()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<String>>() {
                    override fun onError(e: Throwable) {
                        Toast.makeText(this@MapActivity, "error", Toast.LENGTH_SHORT).show()
                    }

                    override fun onNext(t: Response<String>) {
                        Toast.makeText(this@MapActivity, "ok", Toast.LENGTH_SHORT).show()
                    }

                    override fun onCompleted() {

                    }

                })
    }

    override fun showFab() {
        binding.fab.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        binding.fabListener = this
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        binding.fabListener = null
        presenter.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        presenter.onDestroy()
    }

    override fun onFabClicked(v: View) {
        v.isEnabled = false
        presenter.fabClicked()
        v.isEnabled = true
    }

    override fun startAddEventActivity(lng: Double, lat: Double) {
        AddEventActivity.start(this, lng, lat)
    }

}
