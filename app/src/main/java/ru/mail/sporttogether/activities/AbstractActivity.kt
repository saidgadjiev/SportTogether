package ru.mail.sporttogether.activities

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ru.mail.sporttogether.mvp.views.IView

/**
 * Created by bagrusss on 30.09.16.
 *
 */
abstract class AbstractActivity : AppCompatActivity(), IView {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun showProgressDialog() {

    }

    override fun hideProgressDialog() {
        
    }

    @CallSuper
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}