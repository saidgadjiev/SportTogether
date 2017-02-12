package ru.mail.sporttogether.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.presenter.EventDetailsPresenter
import ru.mail.sporttogether.activities.presenter.EventDetailsPresenterImpl
import ru.mail.sporttogether.activities.view.EventDetailsView
import ru.mail.sporttogether.databinding.ActivityEventDetailsBinding
import ru.mail.sporttogether.mvp.PresenterActivity

class EventDetailsActivity : PresenterActivity<EventDetailsPresenter>(), EventDetailsView {

    private lateinit var binding: ActivityEventDetailsBinding

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_details)

        toolbar = binding.toolbar
        setupToolbar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->

        }
        presenter = EventDetailsPresenterImpl(this)
    }
}
