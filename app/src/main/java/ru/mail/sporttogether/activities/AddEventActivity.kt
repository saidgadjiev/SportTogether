package ru.mail.sporttogether.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import ru.mail.sporttogether.R
import ru.mail.sporttogether.data.binding.event.EventData
import ru.mail.sporttogether.databinding.ActivityAddEventBinding
import ru.mail.sporttogether.net.models.Category
import ru.mail.sporttogether.net.responses.Response
import ru.mail.sporttogether.net.utils.RetrofitFactory
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.ArrayList

class AddEventActivity : AbstractActivity() {

    private val bindingData = EventData()

    private lateinit var lat: String
    private lateinit var lng: String

    private lateinit var binding: ActivityAddEventBinding

    private val api = RetrofitFactory.API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_event)
        binding.data = bindingData
        with(intent) {
            lat = getDoubleExtra(KEY_LAT, 0.0).toString()
            lng = getDoubleExtra(KEY_LNG, 0.0).toString()
        }
        bindingData.lat.set(lat)
        bindingData.lng.set(lng)
    }

    private fun updateCategories() {
        api.getAllCategoryes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<ArrayList<Category>>>() {
                    override fun onNext(t: Response<ArrayList<Category>>) {

                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {

                    }

                })
    }

    companion object {

        fun start(c: Context, lng: Double, lat: Double) {
            val intent = Intent(c, AddEventActivity::class.java)
            intent.putExtra(KEY_LNG, lng)
            intent.putExtra(KEY_LAT, lat)
            c.startActivity(intent)
        }

        private val KEY_LNG = "lng"
        private val KEY_LAT = "lat"
    }
}
