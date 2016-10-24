package ru.mail.sporttogether.activities

import android.content.Intent
import android.os.Bundle
import com.auth0.android.lock.Lock
import ru.mail.sporttogether.mvp.presenters.auth.ILoginPresenter
import ru.mail.sporttogether.mvp.presenters.auth.LoginActivityPresenterImpl
import ru.mail.sporttogether.mvp.presenters.auth.auth0.CustomLockActivity
import ru.mail.sporttogether.mvp.views.login.ILoginView

class LoginActivity: PresenterActivity<ILoginPresenter>(), ILoginView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = LoginActivityPresenterImpl(this)
        presenter.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun startMainActivity() {
        startActivity(Intent(this, DrawerActivity::class.java))
        finish()
    }

    override fun startLockActivity(lock: Lock?) {
        val lockIntent = Intent(this, CustomLockActivity::class.java)
        
        lockIntent.putExtra("com.auth0.android.lock.key.Options", lock?.options)
        startActivity(lockIntent)
    }
}
