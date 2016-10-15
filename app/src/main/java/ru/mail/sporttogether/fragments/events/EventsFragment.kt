package ru.mail.sporttogether.fragments.events

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.activities.AbstractActivity
import ru.mail.sporttogether.databinding.FragmentEventsBinding
import ru.mail.sporttogether.fragments.PresenterFragment
import ru.mail.sporttogether.mvp.presenters.event.EventsFragmentPresenter
import ru.mail.sporttogether.mvp.presenters.event.EventsFragmentPresenterImpl

/**
 * Created by bagrusss on 08.10.16.
 *
 */
class EventsFragment() : PresenterFragment<EventsFragmentPresenter>() {

    private lateinit var adapter: FragmentStatePagerAdapter
    private lateinit var binding: FragmentEventsBinding
    private lateinit var pager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEventsBinding.inflate(inflater, container, false)
        presenter = EventsFragmentPresenterImpl()
        pager = binding.pager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val fragmentManager = (activity as AbstractActivity).supportFragmentManager
        adapter = EventsFragmentAdapter(fragmentManager, binding.tab)
        pager.adapter = adapter
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tab))
        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    companion object {
        fun newInstance() = EventsFragment()
    }

}