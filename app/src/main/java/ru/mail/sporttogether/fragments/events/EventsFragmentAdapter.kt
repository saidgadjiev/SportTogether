package ru.mail.sporttogether.fragments.events

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.mail.sporttogether.R

/**
 * Created by bagrusss on 08.10.16.
 *
 */
class EventsFragmentAdapter : FragmentStatePagerAdapter {

    private lateinit var tabs: TabLayout

    constructor(fm: FragmentManager, tabs: TabLayout) : super(fm) {
        this.tabs = tabs
        tabs.addTab(tabs.newTab().setText(R.string.events_map))
        tabs.addTab(tabs.newTab().setText(R.string.events_list))
        tabs.addTab(tabs.newTab().setText(R.string.my_events))
        tabs.tabGravity = TabLayout.GRAVITY_FILL
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> EventsMapFragment.newInstance(tabs.height)
            1 -> EventsListFragment()
            2 -> MyEventsFragment()
            else -> throw IllegalStateException("unknown position of fragment $position")
        }
    }

    override fun getCount() = tabs.tabCount


}