package ru.mail.sporttogether.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.Button
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.presenter.LoginActivityPresenter
import ru.mail.sporttogether.activities.presenter.LoginActivityPresenterImpl
import ru.mail.sporttogether.activities.view.LoginView
import ru.mail.sporttogether.databinding.ActivityLoginBinding
import ru.mail.sporttogether.managers.HeaderManagerImpl
import ru.mail.sporttogether.mvp.PresenterActivity
import javax.inject.Inject

class LoginActivity : PresenterActivity<LoginActivityPresenter>(), LoginView {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var vk: Button
    private lateinit var facebook: Button
    private lateinit var google: Button
    @Inject lateinit var headerManager: HeaderManagerImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        vk = binding.vk
        facebook = binding.facebook
        google = binding.google

        vk.setOnClickListener(loginClick)
        facebook.setOnClickListener(loginClick)
        google.setOnClickListener(loginClick)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = "Авторизация"

        presenter = LoginActivityPresenterImpl(this)
        presenter.onCreate(savedInstanceState)

        val withLogout = intent.extras[WITH_LOGOUT] as Boolean
        if (withLogout) {
            presenter.logoutFromServer()
        }
    }

    private val loginClick = View.OnClickListener { view ->
        presenter.loginClick(view)
    }

    override fun startMainActivity() {
        startActivity(Intent(this, DrawerActivity::class.java))
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        presenter.onActivityResult(requestCode, resultCode, data)
    }


    companion object {
        val WITH_LOGOUT = "with_logout"
        @JvmStatic val TAG = "LoginActivity"

        fun startActivity(context: Context) {
            startActivity(context, false)
        }

        fun startActivity(context: Context, withLogout: Boolean) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(WITH_LOGOUT, withLogout)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}