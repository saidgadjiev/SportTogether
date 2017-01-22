package ru.mail.sporttogether.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import ru.mail.sporttogether.R
import ru.mail.sporttogether.mvp.presenters.splash.SplashActivityPresenter
import ru.mail.sporttogether.mvp.presenters.splash.SplashActivityPresenterImpl
import ru.mail.sporttogether.mvp.views.ISplashView


class SplashActivity :
        PresenterActivity<SplashActivityPresenter>(),
        ISplashView {

    override fun getActivity(): Activity {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("#MY ", "on create in splash activity")
        setContentView(R.layout.activity_splash)
        presenter = SplashActivityPresenterImpl(this)
        presenter.onCreate(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("#MY ", "on new intent in splash activity")
    }

    override fun startMainActivity() {
        finish()
        startActivity(Intent(this, DrawerActivity::class.java))
    }

    override fun startLoginActivity() {
        Handler().postDelayed({
            LoginActivity.startActivity(this)
        }, 1000)
    }

    override fun close() {
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onActivityResult(requestCode, resultCode, data)
    }
}
