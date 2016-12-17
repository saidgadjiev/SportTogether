package ru.mail.sporttogether.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import ru.mail.sporttogether.R
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.databinding.ActivityDrawerBinding
import ru.mail.sporttogether.fragments.events.EventsMapFragment
import ru.mail.sporttogether.fragments.events.MyEventsFragment
import ru.mail.sporttogether.mvp.presenters.drawer.DrawerPresenterImpl
import ru.mail.sporttogether.mvp.presenters.drawer.IDrawerPresenter
import ru.mail.sporttogether.mvp.views.drawer.IDrawerView
import ru.mail.sporttogether.utils.MyDrawerImageLoader


class DrawerActivity : IDrawerView, PresenterActivity<IDrawerPresenter>() {
    private lateinit var binding: ActivityDrawerBinding
    private lateinit var toolbar: Toolbar
    private lateinit var mDrawer: Drawer
    private lateinit var socialNetworkManager: SocialNetworkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("#MY " + this.javaClass.simpleName, "in on create")

        presenter = DrawerPresenterImpl(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_drawer)
        toolbar = binding.drawerToolbar
        setSupportActionBar(toolbar)
        setupToolbar(toolbar)
        buildDrawer()
        socialNetworkManager = SocialNetworkManager.instance
        swapFragment(EventsMapFragment.newInstance(0))
    }

    private fun buildDrawer() {
        val drawerBuilder: DrawerBuilder = DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(buildAccountHeader(this))
                .withToolbar(toolbar)
        setDrawerItems(drawerBuilder)
        mDrawer = drawerBuilder.build()
    }

    private fun buildAccountHeader(activity: DrawerActivity): AccountHeader {
        DrawerImageLoader.init(MyDrawerImageLoader())
        var avatar = SocialNetworkManager.instance.activeUser.avatar
        var name = SocialNetworkManager.instance.activeUser.name
        if (name.isNullOrEmpty())
            name = "No name"
        if (avatar.isNullOrEmpty())
            avatar = "https://scontent-amt2-1.xx.fbcdn.net/v/t1.0-9/14225377_107564209701561_5320272900042567420_n.jpg?oh=efa955d0b647185a747f44f6ce54d390&oe=58B58ED2"
        return AccountHeaderBuilder()
                .withActivity(activity)
                .withCompactStyle(true)
                .withHeaderBackground(R.drawable.drawer_background)
                .withTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .addProfiles(
                        ProfileDrawerItem().withName(name).withIcon(avatar).withTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                )
                .withCloseDrawerOnProfileListClick(false)
                .withSelectionListEnabled(false)
                .build()

    }

    private fun setDrawerItems(drawerBuilder: DrawerBuilder) {
        drawerBuilder.addDrawerItems(
                //TODO add icons
                PrimaryDrawerItem().withName(R.string.map).withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    swapFragment(EventsMapFragment.newInstance(0))
                    title = getString(R.string.events_map)
                    false
                },
                PrimaryDrawerItem().withName(R.string.my_events).withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    swapFragment(MyEventsFragment.newInstance())
                    title = getString(R.string.my_events)
                    false
                },
                PrimaryDrawerItem().withName("Настройки").withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    title = "Настройки"
                    false
                },
                PrimaryDrawerItem().withName("Выход").withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    logout()
                    true
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

    private fun swapFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.drawer_container, fragment)
                .commit()
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        val searchItem = menu.findItem(R.id.action_search)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView = searchItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return super.onCreateOptionsMenu(menu)
    }*/


    override fun startLoginActivity() {
        startActivity(Intent(this, SplashActivity::class.java))
    }
}