package ru.mail.sporttogether.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import ru.mail.sporttogether.R
import ru.mail.sporttogether.auth.core.SocialNetworkError
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.auth.core.listeners.OnInitializationCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.socialNetworks.FacebookSocialNetwork
import ru.mail.sporttogether.auth.core.socialNetworks.VKSocialNetwork
import ru.mail.sporttogether.databinding.ActivityLoginBinding
import ru.mail.sporttogether.managers.headers.HeaderManagerImpl
import ru.mail.sporttogether.mvp.presenters.auth.ILoginPresenter
import ru.mail.sporttogether.mvp.views.login.ILoginView
import javax.inject.Inject

class LoginActivity: PresenterActivity<ILoginPresenter>(), ILoginView, android.support.v4.app.FragmentManager.OnBackStackChangedListener, OnLoginCompleteListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var vk: Button
    private lateinit var google: Button
    private lateinit var facebook: Button
    private lateinit var socialNetworkManager: SocialNetworkManager
    @Inject lateinit var headerManager: HeaderManagerImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        vk = binding.vk
        google = binding.google
        facebook = binding.facebook
        vk.setOnClickListener(loginClick)
        google.setOnClickListener(loginClick)
        facebook.setOnClickListener(loginClick)

        socialNetworkManager = SocialNetworkManager.getInstance()

        presenter.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        socialNetworkManager.onResume()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private val loginClick = View.OnClickListener { view ->
        when (view.id) {
            R.id.facebook -> socialNetworkManager.facebookSocialNetwork.login(this, this)
            R.id.vk -> socialNetworkManager.vkSocialNetwork.login(this, this)
        }
    }

    override fun startMainActivity() {
        startActivity(Intent(this,  DrawerActivity::class.java))
        finish()
    }

    override fun onBackStackChanged() {
        homeAsUpByBackStack()
    }

    private fun homeAsUpByBackStack() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if (backStackEntryCount > 0) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        }
    }

    override fun onSuccess() {
        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
        startMainActivity()
    }

    override fun onCancel() {

    }

    override fun onError(socialNetworkError: SocialNetworkError) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        socialNetworkManager.onActivityResult(requestCode, resultCode, data)
    }
}