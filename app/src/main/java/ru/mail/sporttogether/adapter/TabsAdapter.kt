package ru.mail.sporttogether.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.mail.sporttogether.fragments.EndedListFragment
import ru.mail.sporttogether.fragments.MyEventsListFragment
import ru.mail.sporttogether.fragments.OrganizedEventsListFragment

/**
 * Created by bagrusss on 30.01.17
 */
class TabsAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int) =
            when (position) {
                0 -> MyEventsListFragment()
                1 -> EndedListFragment()
                2 -> OrganizedEventsListFragment()
                else -> {
                    throw IllegalStateException("unknow fragment")
                }
            }


    override fun getCount() = 3
}