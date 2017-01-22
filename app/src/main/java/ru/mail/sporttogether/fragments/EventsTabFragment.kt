package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.long1.spacetablayout.SpaceTabLayout
import ru.mail.sporttogether.databinding.FragmentEventsTabBinding
import ru.mail.sporttogether.fragments.PresenterFragment
import ru.mail.sporttogether.fragments.EndedListFragment
import ru.mail.sporttogether.fragments.MyEventsFragment
import ru.mail.sporttogether.fragments.OrginizedEventsListFragment
import ru.mail.sporttogether.fragments.presenter.EventsTabPresenter
import java.util.*

/**
 * Created by bagrusss on 05.01.17
 */
class EventsTabFragment : PresenterFragment<EventsTabPresenter>() {

    private lateinit var binding: FragmentEventsTabBinding

    private lateinit var tabLayout: SpaceTabLayout
    private lateinit var viewPager: ViewPager
    private val fragments = ArrayList<Fragment>(3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEventsTabBinding.inflate(inflater)
        tabLayout = binding.spaceTabLayout
        viewPager = binding.viewPager

        fragments.add(MyEventsFragment())
        fragments.add(EndedListFragment())
        fragments.add(OrginizedEventsListFragment())

        tabLayout.initialize(viewPager, childFragmentManager, fragments, savedInstanceState)

        presenter = object : EventsTabPresenter {

        }
        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance() = EventsTabFragment()


    }
}