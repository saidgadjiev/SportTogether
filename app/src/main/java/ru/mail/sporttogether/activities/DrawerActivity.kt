package ru.mail.sporttogether.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.presenter.DrawerActivityPresenter
import ru.mail.sporttogether.activities.presenter.DrawerActivityPresenterImpl
import ru.mail.sporttogether.activities.view.DrawerView
import ru.mail.sporttogether.data.binding.DrawerData
import ru.mail.sporttogether.databinding.ActivityDrawerBinding
import ru.mail.sporttogether.fragments.EventsTabFragment
import ru.mail.sporttogether.fragments.SettingsFragment
import ru.mail.sporttogether.fragments.TemplatesListFragment
import ru.mail.sporttogether.mvp.PresenterActivity
import ru.mail.sporttogether.utils.DrawerLoader


class DrawerActivity : DrawerView,
        PresenterActivity<DrawerActivityPresenter>() {
    private lateinit var binding: ActivityDrawerBinding
    private lateinit var toolbar: Toolbar
    private lateinit var drawer: Drawer
    private var lastPoss = 0
    private val drawerData = DrawerData()
    private lateinit var mapItem: PrimaryDrawerItem
    private var item: MenuItem? = null
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("#MY ", "on create in drawer activity")
        isCreated = true

        presenter = DrawerActivityPresenterImpl(this)
        if (!presenter.checkIsUserInited()) {
            startSplashActivity()
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_drawer)
            toolbar = binding.drawerToolbar
            binding.drawerData = drawerData
            setSupportActionBar(toolbar)
            setupToolbar(toolbar)
            buildDrawer()
            toolbar.title = getString(R.string.events_map)
            val bundle = intent.getBundleExtra("data")
            Log.d("#MY DrawerActivity", "bundle " + bundle)
            if (bundle != null) {
                presenter.showEventOnMap(bundle)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        isCreated = false
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("#MY ", "on new intent in drawer activity")
        val bundle = intent?.getBundleExtra("data")
        if (bundle != null) {
            presenter.showEventOnMap(bundle)
        }
    }

    private fun buildDrawer() {
        val drawerBuilder: DrawerBuilder = DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(buildAccountHeader())
                .withToolbar(toolbar)
        setDrawerItems(drawerBuilder)
        drawer = drawerBuilder.build()
    }

    private fun buildAccountHeader(): AccountHeader {
        DrawerImageLoader.init(DrawerLoader())
        var name = presenter.getProfileName()
        var avatar = presenter.getAvatar()
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


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)
        item = menu.findItem(R.id.action_search)
        return true
    }

    private fun setDrawerItems(drawerBuilder: DrawerBuilder) {
        mapItem = PrimaryDrawerItem().withName(R.string.map).withIcon(R.drawable.ic_map).withIconTintingEnabled(true).withOnDrawerItemClickListener { view, i, iDrawerItem ->
            //swapFragment(EventsMapFragment.newInstance(0), i)
            showMap()
            toolbar.title = getString(R.string.events_map)
            false
        }
        drawerBuilder.addDrawerItems(
                mapItem,
                PrimaryDrawerItem().withName(R.string.my_events).withIcon(R.drawable.ic_location).withIconTintingEnabled(true).withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    swapFragment(EventsTabFragment(), i)
                    toolbar.title = getString(R.string.my_events)
                    false
                },
                PrimaryDrawerItem().withName(R.string.templates).withIcon(R.drawable.ic_templates).withIconTintingEnabled(true).withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    swapFragment(TemplatesListFragment(), i)
                    toolbar.title = getString(R.string.templates)
                    false
                },
                PrimaryDrawerItem().withName(R.string.settings).withIcon(R.drawable.ic_settings).withIconTintingEnabled(true).withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    swapFragment(SettingsFragment(), i)
                    toolbar.title = getString(R.string.settings)
                    false
                },
                DividerDrawerItem(),
                PrimaryDrawerItem().withName(R.string.exit).withIcon(R.drawable.ic_exit).withIconTintingEnabled(true).withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    logout()
                    true
                }
        )
    }

    fun logout() {
        presenter.logout()
    }

    private fun swapFragment(fragment: Fragment, newPos: Int) {
        handler.postDelayed({
            if (lastPoss != newPos) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.drawer_container, fragment)
                        .commit()
                lastPoss = newPos
                drawerData.mapActive.set(false)
                item?.isVisible = false
            }
        }, 100)
    }

    fun showMap() {
        if (lastPoss != 0) {
            drawerData.mapActive.set(true)
            val fragment = supportFragmentManager.findFragmentById(R.id.drawer_container)
            supportFragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
            lastPoss = 0
            drawer.setSelection(mapItem)
            item?.isVisible = true
        }
    }

    override fun startLoginActivity() {
        LoginActivity.startActivity(this, true)
    }

    override fun startSplashActivity() {
        Log.d("#MY ", "started splash activity")
        finish()
        SplashActivity.startActivity(this)
    }

    companion object {
        var isCreated = false
        @JvmStatic val TAG = "DrawerActivity"
    }
}