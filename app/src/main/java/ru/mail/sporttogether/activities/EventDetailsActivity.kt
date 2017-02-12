package ru.mail.sporttogether.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.Toolbar
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_details)
        binding.data = data

        toolbar = binding.toolbar
        setupToolbar(toolbar)

        presenter = EventDetailsPresenterImpl(this)

        intent.getParcelableExtra<Event>(KEY_EVENT)?.let { event ->
            data.date.set(DateUtils.longToDateTime(event.date))
            data.description.set(event.result ?: event.description)
            data.organizerName.set(event.user.name)
            val peopleNow = event.nowPeople
            val peopleMax = event.maxPeople
            data.people.set("$peopleNow/$peopleMax")
            data.sport.set(event.category.name)
            toolbar.title = event.name
        }
    }

    override fun onStart() {
        super.onStart()
        binding.listener = this
    }

    override fun onStop() {
        super.onStop()
        binding.listener = null
    }

    override fun onFabClicked() {

    }

    override fun onUsersClicked() {

    }

    companion object {
        @JvmStatic private val KEY_EVENT = "event"

        @JvmStatic
        fun start(context: Context, event: Event) {
            val intent = Intent(context, EventDetailsActivity::class.java)
            intent.putExtra(KEY_EVENT, event)
            context.startActivity(intent)
        }
    }
}
