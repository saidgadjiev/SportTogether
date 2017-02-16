package ru.mail.sporttogether.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import ru.mail.sporttogether.R
import ru.mail.sporttogether.adapter.TabsAdapter
import ru.mail.sporttogether.databinding.FragmentEventsTabBinding
import java.util.*


/**
 * Created by bagrusss on 05.01.17
 */
class EventsTabFragment : Fragment() {

    private lateinit var binding: FragmentEventsTabBinding

    private lateinit var viewPager: ViewPager
    private val fragments = ArrayList<Fragment>(3)
    private lateinit var tabs: AHBottomNavigation
    private lateinit var tabsAdapter: TabsAdapter
    private val handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEventsTabBinding.inflate(inflater)
        viewPager = binding.viewPager
        tabs = binding.bottomNavigation

        tabsAdapter = TabsAdapter(fragmentManager)
        viewPager.adapter = tabsAdapter
        viewPager.offscreenPageLimit = 2


        val myDrawable = ContextCompat.getDrawable(context, R.drawable.ic_my_location)
        val myColor = ContextCompat.getColor(context, R.color.colorPrimary)

        val organizedDrawable = ContextCompat.getDrawable(context, R.drawable.ic_organized)
        val organizedColor = ContextCompat.getColor(context, R.color.colorAccent)


        val item1 = AHBottomNavigationItem(getString(R.string.my), myDrawable, myColor)
        val item2 = AHBottomNavigationItem(R.string.resulted, R.mipmap.ic_launcher, R.color.gray)
        val item3 = AHBottomNavigationItem(getString(R.string.organized_events), organizedDrawable, Color.parseColor("#8BC34A"))

        tabs.addItem(item1)
        tabs.addItem(item2)
        tabs.addItem(item3)

        tabs.accentColor = ContextCompat.getColor(context, R.color.colorAccent)
        tabs.inactiveColor = ContextCompat.getColor(context, R.color.white)
        tabs.isColored = true

        tabs.setOnTabSelectedListener({ position, wasSelected ->
            handler.post {
                viewPager.currentItem = position
            }
            true
        })
        tabs.setOnNavigationPositionListener({
            // Manage the new y position
        })

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                tabs.currentItem = position
            }

        })


        fragments.add(MyEventsListFragment())
        fragments.add(EndedListFragment())
        fragments.add(OrganizedEventsListFragment())


        return binding.root
    }
}