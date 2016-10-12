package ru.mail.sporttogether.activities.drawer

import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.events.EventsFragment
import ru.mail.sporttogether.fragments.events.MyEventsFragment

/**
 * Created by Ivan on 12.10.2016.
 */
object DrawerManager {
    fun buildAccoundHeader(activity: DrawerActivity): AccountHeader {
        return AccountHeaderBuilder ()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.drawer_background)
                .withTextColor(activity.getColor(R.color.colorAccent))
                .addProfiles(
                        ProfileDrawerItem().withName("Ivan").withEmail("1@1.1").withTextColor(activity.getColor(R.color.colorPrimary)),
                        ProfileDrawerItem().withName("Vlad").withEmail("2@2.2").withTextColor(activity.getColor(R.color.colorPrimary)),
                        ProfileDrawerItem().withName("Said").withEmail("3@3.3").withTextColor(activity.getColor(R.color.colorPrimary))
                )
                .build()

    }

    fun setDrawerItems(drawerBuilder: DrawerBuilder, activity: DrawerActivity) {
        val supportFragmentManager = activity.supportFragmentManager
        drawerBuilder.addDrawerItems(
                //TODO add icons
                PrimaryDrawerItem().withName("Карта").withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.drawer_container, EventsFragment.newInstance())
                            .commit()
                    println("Clicked : " + i)
                    false
                },
                PrimaryDrawerItem().withName("Мои события").withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.drawer_container, MyEventsFragment.newInstance())
                            .commit()
                    println("Clicked : " + i)
                    false
                },
                PrimaryDrawerItem().withName("Настройки").withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    println("Clicked : " + i)
                    false
                }
        )
    }
}