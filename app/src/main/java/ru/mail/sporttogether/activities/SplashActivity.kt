package ru.mail.sporttogether.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.util.Log
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.presenter.SplashActivityPresenter
import ru.mail.sporttogether.activities.presenter.SplashActivityPresenterImpl
import ru.mail.sporttogether.activities.view.SplashView
import ru.mail.sporttogether.data.binding.SplashData
import ru.mail.sporttogether.databinding.ActivitySplashBinding
import ru.mail.sporttogether.mvp.PresenterActivity

class SplashActivity :
        PresenterActivity<SplashActivityPresenter>(),
        SplashView {

    private var bundle: Bundle? = null
    private val data = SplashData()
    private lateinit var binding: ActivitySplashBinding

    override fun getActivity(): Activity {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.data = data
        data.text.set(getString(R.string.app_name).toUpperCase())
        presenter = SplashActivityPresenterImpl(this)
        presenter.onCreate(savedInstanceState)
        bundle = intent.getBundleExtra("data")
        Log.d("#MY SplashActivity", "on create bundle " + bundle)
    }

    override fun startMainActivity() {
        finish()
        val intent = Intent(this, DrawerActivity::class.java)
        Log.d("#MY SplashActivity", "bundle " + bundle)
        if (bundle != null) {
            intent.putExtra("data", bundle)
            bundle = null
        }
        startActivity(intent)
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

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}
