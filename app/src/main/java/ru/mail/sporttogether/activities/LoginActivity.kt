package ru.mail.sporttogether.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.Button
import ru.mail.sporttogether.R
import ru.mail.sporttogether.databinding.ActivityLoginBinding
import ru.mail.sporttogether.managers.headers.HeaderManagerImpl
import ru.mail.sporttogether.mvp.presenters.auth.ILoginPresenter
import ru.mail.sporttogether.mvp.presenters.auth.LoginPresenterImpl
import ru.mail.sporttogether.mvp.views.login.ILoginView
import javax.inject.Inject



class LoginActivity: PresenterActivity<ILoginPresenter>(), ILoginView {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var vk: Button
    private lateinit var facebook: Button
    @Inject lateinit var headerManager: HeaderManagerImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        vk = binding.vk
        facebook = binding.facebook
        vk.setOnClickListener(loginClick)
        facebook.setOnClickListener(loginClick)

        presenter = LoginPresenterImpl(this)
        presenter.onCreate(savedInstanceState)

        val withLogout = intent.extras[WITH_LOGOUT] as Boolean
        if (withLogout) {
            presenter.logoutFromServer()
        }
    }

    override fun onResume() {
        super.onResume()

        presenter.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.onDestroy()
    }

    private val loginClick = View.OnClickListener { view ->
        presenter.loginClick(view)
    }

    override fun startMainActivity() {
        startActivity(Intent(this,  DrawerActivity::class.java))
        finish()
    }

//    override fun onBackStackChanged() {
//        homeAsUpByBackStack()
//    }
//
//    private fun homeAsUpByBackStack() {
//        val backStackEntryCount = supportFragmentManager.backStackEntryCount
//        if (backStackEntryCount > 0) {
//            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        } else {
//            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        presenter.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        val WITH_LOGOUT = "with_logout"

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