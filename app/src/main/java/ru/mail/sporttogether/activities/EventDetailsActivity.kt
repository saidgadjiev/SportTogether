package ru.mail.sporttogether.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.gms.maps.MapView
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.presenter.EventDetailsPresenter
import ru.mail.sporttogether.activities.presenter.EventDetailsPresenterImpl
import ru.mail.sporttogether.activities.view.EventDetailsView
import ru.mail.sporttogether.data.binding.EventDetailData
import ru.mail.sporttogether.databinding.ActivityEventDetailsBinding
import ru.mail.sporttogether.mvp.PresenterActivity
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.utils.DateUtils

class EventDetailsActivity :
        PresenterActivity<EventDetailsPresenter>(),
        EventDetailsView {

    private lateinit var binding: ActivityEventDetailsBinding
    private val data = EventDetailData()

    private lateinit var toolbar: Toolbar
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var mapView: MapView
    private var event: Event? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_details)
        binding.data = data

        collapsingToolbarLayout = binding.toolbarLayout
        toolbar = binding.toolbar
        mapView = binding.mapView

        setupToolbar(toolbar)
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.black_universal))


        presenter = EventDetailsPresenterImpl(this)

        event = intent.getParcelableExtra<Event>(KEY_EVENT)?.let { event ->
            data.isEventDetails.set(intent.getBooleanExtra(KEY_IS_EVENT, true))
            data.date.set(DateUtils.longToDateTime(event.date))
            data.description.set(event.result ?: event.description)
            data.organizerName.set(event.user.name)
            val peopleNow = event.nowPeople
            val peopleMax = event.maxPeople
            data.people.set("$peopleNow/$peopleMax")
            data.sport.set(event.category.name)
            collapsingToolbarLayout.title = event.name
            presenter.onCreate(event, savedInstanceState)
            presenter.loadAddress(event.lat, event.lng)
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync(presenter)
            Glide.with(this)
                    .load(event.user.avatar)
                    .into(binding.include.userPic)
            event
        }

    }

    override fun onStart() {
        super.onStart()
        //mapView.onStart()
        binding.listener = this
    }

    override fun onStop() {
        super.onStop()
        //mapView.onStop()
        binding.listener = null
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onFabClicked() {
        if (data.isEventDetails.get())
            presenter.onFabClicked()
        else {
            event?.let {
                AddEventActivity.start(this, it)
            }
        }
        onBackPressed()
    }

    override fun onUsersClicked() {

    }

    override fun updateAddress(address: String) {
        data.address.set(address)
    }

    override fun onMapReady() {
        data.mapReady.set(true)
    }

    companion object {
        @JvmStatic private val KEY_EVENT = "event"
        @JvmStatic private val KEY_IS_EVENT = "is_event"

        @JvmStatic
        fun startEvent(context: Context, event: Event) {
            val intent = Intent(context, EventDetailsActivity::class.java)
            intent.putExtra(KEY_EVENT, event)
            context.startActivity(intent)
        }

        @JvmStatic
        fun startTemplate(context: Context, event: Event) {
            val intent = Intent(context, EventDetailsActivity::class.java)
            intent.putExtra(KEY_EVENT, event)
            intent.putExtra(KEY_IS_EVENT, false)
            context.startActivity(intent)
        }
    }
}
