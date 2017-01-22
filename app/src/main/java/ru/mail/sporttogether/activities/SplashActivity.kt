package ru.mail.sporttogether.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.presenter.SplashActivityPresenter
import ru.mail.sporttogether.activities.presenter.SplashActivityPresenterImpl
import ru.mail.sporttogether.activities.view.SplashView



class SplashActivity :
        PresenterActivity<SplashActivityPresenter>(),
        SplashView {

    override fun getActivity(): Activity {
        return this
    }

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
