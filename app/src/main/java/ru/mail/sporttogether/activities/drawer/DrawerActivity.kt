package ru.mail.sporttogether.activities.drawer

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.PresenterActivity
import ru.mail.sporttogether.databinding.ActivityDrawerBinding
import ru.mail.sporttogether.fragments.events.EventsFragment
import ru.mail.sporttogether.mvp.presenters.DrawerPresenter

class DrawerActivity : PresenterActivity<DrawerPresenter>() {

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
                .withAccountHeader(DrawerManager.buildAccoundHeader(this))
                .withToolbar(toolbar)
        DrawerManager.setDrawerItems(drawerBuilder, this)
        mDrawer = drawerBuilder.build()
    }
}