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

    constructor(fm: FragmentManager, tabs: TabLayout) : super(fm) {
        tabs.addTab(tabs.newTab().setText(R.string.events_map))
        tabs.addTab(tabs.newTab().setText(R.string.events_list))
        tabs.tabGravity = TabLayout.GRAVITY_CENTER
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> EventsMapFragment()
            else -> EventsListFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

}