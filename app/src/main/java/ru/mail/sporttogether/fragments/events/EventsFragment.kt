package ru.mail.sporttogether.fragments.events

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import ru.mail.sporttogether.activities.PresenterActivity
import ru.mail.sporttogether.databinding.FragmentEventsBinding
import ru.mail.sporttogether.fragments.PresenterFragment
import ru.mail.sporttogether.mvp.presenters.event.EventsFragmentImpl
import ru.mail.sporttogether.mvp.presenters.event.EventsPresenter

/**
 * Created by bagrusss on 08.10.16.
 *
 */
class EventsFragment() : PresenterFragment<EventsPresenter>() {

    private lateinit var adapter: FragmentStatePagerAdapter
    private lateinit var binding: FragmentEventsBinding
    private lateinit var pager: ViewPager
    private lateinit var tab: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEventsBinding.inflate(inflater, container, false)
        presenter = EventsFragmentImpl()
        pager = binding.pager
        tab = binding.tab
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val fragmentManager = (activity as PresenterActivity<*>).supportFragmentManager
        // попробовать  childFragmentManager для получения onPermissionResult
        val viewTreeObserver = tab.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    adapter = EventsFragmentAdapter(fragmentManager, binding.tab)
                    pager.adapter = adapter
                    pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tab))
                }
            })
        }
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

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