package ru.mail.sporttogether.activities.drawer

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.AbstractActivity
import ru.mail.sporttogether.databinding.ActivityDrawerBinding
import ru.mail.sporttogether.fragments.events.EventsFragment
import ru.mail.sporttogether.fragments.events.MyEventsFragment
import ru.mail.sporttogether.mvp.presenters.drawer.DrawerActivityPresenter

class DrawerActivity : AbstractActivity() {

    private lateinit var presenter: DrawerActivityPresenter
    private lateinit var binding: ActivityDrawerBinding
    private lateinit var toolbar: Toolbar
    private lateinit var mDrawer: Drawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("#MY " + this.javaClass.simpleName, "in on create")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_drawer)
        toolbar = binding.drawerToolbar
        setSupportActionBar(toolbar)
        setupToolbar(toolbar)
        supportFragmentManager.beginTransaction()
                .replace(R.id.drawer_container, EventsFragment.newInstance())
                .commit()

        val drawerBuilder: DrawerBuilder = DrawerBuilder()
            .withActivity(this)
            .withAccountHeader(buildAccoundHeader())
            .withToolbar(toolbar)
        setDrawerItems(drawerBuilder)
        mDrawer = drawerBuilder.build()
    }

    fun buildAccoundHeader(): AccountHeader {
        return AccountHeaderBuilder ()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawer_background)
                .withTextColor(this.getColor(R.color.colorAccent))
                .addProfiles(
                        ProfileDrawerItem().withName("Ivan").withEmail("1@1.1").withTextColor(this.getColor(R.color.colorPrimary)),
                        ProfileDrawerItem().withName("Vlad").withEmail("2@2.2").withTextColor(this.getColor(R.color.colorPrimary)),
                        ProfileDrawerItem().withName("Said").withEmail("3@3.3").withTextColor(this.getColor(R.color.colorPrimary))
                )
                .build()

    }

    fun setDrawerItems(drawerBuilder: DrawerBuilder) {
        val supportFragmentManager = this.supportFragmentManager
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
                },
                PrimaryDrawerItem().withName("Выход").withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    println("Clicked : " + i)
                    false
                }
        )
    }
}