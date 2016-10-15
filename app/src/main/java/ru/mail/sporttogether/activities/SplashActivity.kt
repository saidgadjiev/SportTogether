package ru.mail.sporttogether.activities

import android.os.Bundle
import ru.mail.sporttogether.R
import ru.mail.sporttogether.mvp.presenters.SplashActivityPresenter

class SplashActivity : PresenterActivity<SplashActivityPresenter>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
}
