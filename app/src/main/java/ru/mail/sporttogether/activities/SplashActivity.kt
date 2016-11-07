package ru.mail.sporttogether.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.auth0.android.lock.Lock
import ru.mail.sporttogether.R
import ru.mail.sporttogether.mvp.presenters.splash.SplashActivityPresenter
import ru.mail.sporttogether.mvp.presenters.splash.SplashActivityPresenterImpl
import ru.mail.sporttogether.mvp.views.ISplashView

class SplashActivity :
        PresenterActivity<SplashActivityPresenter>(),
        ISplashView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        presenter = SplashActivityPresenterImpl(this)
        presenter.onCreate(savedInstanceState)
    }

    override fun startMainActivity() {
        finish()
        startActivity(Intent(this, DrawerActivity::class.java))
    }

    override fun startLockActivity(lock: Lock?) {
        Handler().postDelayed({
            startActivity(lock?.newIntent(this@SplashActivity))
        }, 1000)
    }

    override fun close() {
        finish()
    }
}
