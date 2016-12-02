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
import ru.mail.sporttogether.fragments.events.EventsFragment
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
        swapFragment(EventsFragment.newInstance())
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
        var  avatar = SocialNetworkManager.instance.activeUser.avatar
        var name = SocialNetworkManager.instance.activeUser.name
        if (name == null || name.isEmpty()) name = "Ivan Semenc"
        if (avatar == null || avatar.isEmpty()) avatar = "https://graph.facebook.com/106396279832921/picture?type=large"
        val instance = SocialNetworkManager.instance
        val socialNetwork = instance.getSocialNetwork(instance.getNetworkID())!!
        val name1 = socialNetwork.getLoadedSocialPerson()!!.name
        val avatar1 = socialNetwork.getLoadedSocialPerson()!!.avatarURL
        return AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.drawer_background)
                .withTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .addProfiles(
                        ProfileDrawerItem().withName(name1).withIcon(avatar1).withTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        )
                .withCloseDrawerOnProfileListClick(false)
                .withSelectionListEnabled(false)
                .build()

    }

    private fun setDrawerItems(drawerBuilder: DrawerBuilder) {
        drawerBuilder.addDrawerItems(
                //TODO add icons
                PrimaryDrawerItem().withName("Карта").withOnDrawerItemClickListener { view, i, iDrawerItem ->
                    swapFragment(EventsFragment.newInstance())
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

    private fun swapFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.drawer_container, fragment)
                .commit()
    }

    override fun startLoginActivity() {
        startActivity(Intent(this, SplashActivity::class.java))
    }
}