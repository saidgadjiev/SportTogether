package ru.mail.sporttogether.fragments.events

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ru.mail.sporttogether.adapter.EventsAdapter
import ru.mail.sporttogether.databinding.FragmentMyEventsBinding
import ru.mail.sporttogether.fragments.PresenterFragment
import ru.mail.sporttogether.mvp.presenters.event.MyEventsPresenter
import ru.mail.sporttogether.mvp.presenters.event.MyEventsPresenterImpl
import ru.mail.sporttogether.mvp.views.event.IListEventView

class MyEventsFragment : PresenterFragment<MyEventsPresenter>(), IListEventView {
    private lateinit var binding: FragmentMyEventsBinding
    private lateinit var mEventsListView: RecyclerView
    private lateinit var mEventsListLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMyEventsBinding.inflate(inflater, container, false)
        Toast.makeText(activity, "wooow!", Toast.LENGTH_SHORT).show()
        presenter = MyEventsPresenterImpl(this)
        mEventsListView = binding.myEventsRecyclerView
        mEventsListLayoutManager = LinearLayoutManager(activity)
        mEventsListView.layoutManager = mEventsListLayoutManager
        presenter.getMyEvents()
        return binding.root
    }

    override fun loadEvents(events: List<String>) {
        mEventsListView.adapter = EventsAdapter(events)
    }

    companion object {
        fun newInstance() = MyEventsFragment()
    }
}