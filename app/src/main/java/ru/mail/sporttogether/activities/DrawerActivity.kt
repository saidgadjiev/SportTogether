package ru.mail.sporttogether.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import ru.mail.sporttogether.R
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.databinding.ActivityDrawerBinding
import ru.mail.sporttogether.fragments.events.EventsFragment
import ru.mail.sporttogether.fragments.events.MyEventsFragment
import ru.mail.sporttogether.mvp.presenters.drawer.DrawerPresenterImpl
import ru.mail.sporttogether.mvp.presenters.drawer.IDrawerPresenter
import ru.mail.sporttogether.mvp.views.drawer.IDrawerView

class DrawerActivity : IDrawerView, PresenterActivity<IDrawerPresenter>() {
    private lateinit var binding: ActivityDrawerBinding
    private lateinit var toolbar: Toolbar
    private lateinit var mDrawer: Drawer
    private lateinit var socialNetworkManager: SocialNetworkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("#MY " + this.javaClass.simpleName, "in on create")

        App.injector.useViewComopnent().inject(this)
        presenter = DrawerPresenterImpl(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_drawer)
        toolbar = binding.drawerToolbar
        setSupportActionBar(toolbar)
        setupToolbar(toolbar)
        buildDrawer()
        socialNetworkManager = SocialNetworkManager.getInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.drawer_container, EventsFragment.newInstance())
                .commit()
    }

    private fun buildDrawer() {
        val drawerBuilder: DrawerBuilder = DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(buildAccoundHeader(this))
                .withToolbar(toolbar)
        setDrawerItems(drawerBuilder, this)
        mDrawer = drawerBuilder.build()
    }

    private fun buildAccoundHeader(activity: DrawerActivity): AccountHeader {
        return AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.drawer_background)
                .withTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .addProfiles(
                        ProfileDrawerItem().withName("Ivan").withEmail("1@1.1").withTextColor(ContextCompat.getColor(this, R.color.colorPrimary)),
                        ProfileDrawerItem().withName("Vlad").withEmail("2@2.2").withTextColor(ContextCompat.getColor(this, R.color.colorPrimary)),
                        ProfileDrawerItem().withName("Said").withEmail("3@3.3").withTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                )
                .withCloseDrawerOnProfileListClick(false)
                .build()

    }

    private fun setDrawerItems(drawerBuilder: DrawerBuilder, activity: DrawerActivity) {
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
                },
                PrimaryDrawerItem().withName("Выход").withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    logout()
                    false
                }
        )
    }

    fun logout() {
        for (network in socialNetworkManager.initializedSocialNetworks) {
            if (network.isConnected) {
                network.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                break
            }
        }
    }

    override fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}