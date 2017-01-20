package ru.mail.sporttogether.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
import ru.mail.sporttogether.fragments.events.EventsTabFragment
import ru.mail.sporttogether.mvp.presenters.drawer.DrawerPresenterImpl
import ru.mail.sporttogether.mvp.presenters.drawer.IDrawerPresenter
import ru.mail.sporttogether.mvp.views.drawer.IDrawerView
import ru.mail.sporttogether.utils.MyDrawerImageLoader


class DrawerActivity : IDrawerView,
        PresenterActivity<IDrawerPresenter>() {
    private lateinit var binding: ActivityDrawerBinding
    private lateinit var toolbar: Toolbar
    private lateinit var mDrawer: Drawer
    private lateinit var socialNetworkManager: SocialNetworkManager
    private var lastPoss = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = DrawerPresenterImpl(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_drawer)
        toolbar = binding.drawerToolbar
        setSupportActionBar(toolbar)
        setupToolbar(toolbar)
        buildDrawer()
        socialNetworkManager = SocialNetworkManager.instance
        toolbar.title = getString(R.string.events_map)
        swapFragment(EventsMapFragment.newInstance(0), lastPoss + 1)
    }

    private fun buildDrawer() {
        val drawerBuilder: DrawerBuilder = DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(buildAccountHeader())
                .withToolbar(toolbar)
        setDrawerItems(drawerBuilder)
        mDrawer = drawerBuilder.build()
    }

    private fun buildAccountHeader(): AccountHeader {
        DrawerImageLoader.init(MyDrawerImageLoader())
        var avatar = SocialNetworkManager.instance.activeUser.avatar
        var name = SocialNetworkManager.instance.activeUser.name
        if (name.isNullOrEmpty())
            name = "No name"
        if (avatar.isNullOrEmpty())
            avatar = "https://scontent-amt2-1.xx.fbcdn.net/v/t1.0-9/14225377_107564209701561_5320272900042567420_n.jpg?oh=efa955d0b647185a747f44f6ce54d390&oe=58B58ED2"
        val header = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.navigation_header)
                .addProfiles(
                        ProfileDrawerItem().withName(name).withIcon(avatar).withTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                )
                .withTextColorRes(R.color.vk_white)
                .withCloseDrawerOnProfileListClick(false)
                .withSelectionListEnabled(false)
                .build()

        Glide.with(this)
                .load(R.drawable.navigation_header)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(header.headerBackgroundView)

        return header

    }

    private fun setDrawerItems(drawerBuilder: DrawerBuilder) {
        drawerBuilder.addDrawerItems(
                //TODO add icons
                PrimaryDrawerItem().withName(R.string.map).withIcon(R.drawable.ic_map).withIconTintingEnabled(true).withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    swapFragment(EventsMapFragment.newInstance(0), i)
                    toolbar.title = getString(R.string.events_map)
                    false
                },
                PrimaryDrawerItem().withName(R.string.my_events).withIcon(R.drawable.ic_location).withIconTintingEnabled(true).withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    swapFragment(EventsTabFragment.newInstance(), i)
                    toolbar.title = getString(R.string.my_events)
                    false
                },
                PrimaryDrawerItem().withName(R.string.settings).withIcon(R.drawable.ic_settings).withIconTintingEnabled(true).withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    toolbar.title = getString(R.string.settings)
                    false
                },
                PrimaryDrawerItem().withName(R.string.exit).withIcon(R.drawable.ic_exit).withIconTintingEnabled(true).withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    logout()
                    true
                }
        )
    }

    fun logout() {
        for (network in socialNetworkManager.initializedSocialNetworks) {
            if (network.isConnected) {
                network.logout()
                startLoginActivity()
                break
            }
        }
    }

    private fun swapFragment(fragment: Fragment, newPos: Int) {
        if (lastPoss != newPos) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.drawer_container, fragment)
                    .commit()
            lastPoss = newPos
        }
    }

    override fun startLoginActivity() {
        LoginActivity.startActivity(this, true)
    }
}