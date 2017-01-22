package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.long1.spacetablayout.SpaceTabLayout
import ru.mail.sporttogether.databinding.FragmentEventsTabBinding
import java.util.*

/**
 * Created by bagrusss on 05.01.17
 */
class EventsTabFragment : Fragment() {

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
        fragments.add(OrganizedEventsListFragment())

        tabLayout.initialize(viewPager, childFragmentManager, fragments, savedInstanceState)

        return binding.root
    }
}