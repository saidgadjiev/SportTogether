package ru.mail.sporttogether.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import com.vk.sdk.VKScope
import ru.mail.sporttogether.R
import ru.mail.sporttogether.auth.core.AuthError
import ru.mail.sporttogether.auth.core.SocialNetwork.LoginCallback
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.auth.facebook.FacebookSocialNetwork
import ru.mail.sporttogether.auth.google.GoogleSocialNetwork
import ru.mail.sporttogether.auth.vk.VKSocialNetwork
import ru.mail.sporttogether.databinding.ActivityLoginBinding
import ru.mail.sporttogether.mvp.presenters.auth.ILoginPresenter
import ru.mail.sporttogether.mvp.presenters.auth.LoginActivityPresenterImpl
import ru.mail.sporttogether.mvp.views.login.ILoginView

class LoginActivity: PresenterActivity<ILoginPresenter>(), ILoginView, android.support.v4.app.FragmentManager.OnBackStackChangedListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var vk: Button
    private lateinit var google: Button
    private lateinit var facebook: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        vk = binding.vk
        google = binding.google
        facebook = binding.facebook
        vk.setOnClickListener(loginClick)
        google.setOnClickListener(loginClick)
        facebook.setOnClickListener(loginClick)
        val vkNetwork = VKSocialNetwork(this, arrayOf(VKScope.OFFLINE), callback)
        val googleNetwork = GoogleSocialNetwork(this, getString(R.string.google_server_client_id), arrayOf(Scope(Scopes.PLUS_LOGIN)), callback)
        val facebookNetwork = FacebookSocialNetwork(this, arrayListOf("public_profile"), callback)

        SocialNetworkManager.getInstance().addSocialNetwork(vkNetwork)
        SocialNetworkManager.getInstance().addSocialNetwork(googleNetwork)
        SocialNetworkManager.getInstance().addSocialNetwork(facebookNetwork)

        presenter = LoginActivityPresenterImpl(this)
        presenter.onCreate(savedInstanceState)
        if (tryLogin()) {
            startMainActivity()
        }
    }

    private fun tryLogin(): Boolean {
        val networkID = SocialNetworkManager.getInstance().sharedPreferces.getInt(SocialNetworkManager.AUTH_ID, 0)

        if (SocialNetworkManager.getInstance().registeredNetworks.containsKey(networkID)) {
            val socialNetwork = SocialNetworkManager.getInstance().getSocialNetwork(networkID)

            return socialNetwork.isLoggedIn
        }

        return false
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private val loginClick = View.OnClickListener { view ->
        var networkId = 0
        when (view.id) {
            R.id.vk -> networkId = VKSocialNetwork.ID
            R.id.google -> networkId = GoogleSocialNetwork.ID
            R.id.facebook -> networkId = FacebookSocialNetwork.ID
        }
        val socialNetwork = SocialNetworkManager.getInstance().getSocialNetwork(networkId)

        if (!socialNetwork.isLoggedIn) {
            socialNetwork.login(this)
        }
    }

    override fun startMainActivity() {
        startActivity(Intent(this, TestActivity::class.java))
        finish()
    }

    var callback = object : LoginCallback {
        override fun onCancel() {
        }

        override fun onOK() {
            startMainActivity()
        }

        override fun onError(error: AuthError) {
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        SocialNetworkManager.getInstance().authSocialNetwork.isLoginOK(requestCode, resultCode, data)
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
}