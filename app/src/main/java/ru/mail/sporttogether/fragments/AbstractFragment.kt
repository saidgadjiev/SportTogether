package ru.mail.sporttogether.fragments

import android.support.annotation.CallSuper
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import ru.mail.sporttogether.activities.AbstractActivity
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.dagger.components.AppMainComponent
import ru.mail.sporttogether.mvp.views.IView

/**
 * Created by bagrusss on 08.10.16.
 *
 */
abstract class AbstractFragment : Fragment(), IView {

    protected val injector = App.injector

    override fun showToast(message: String, duration: Int) {
        Toast.makeText(activity, message, duration).show()
    }

    override fun showToast(@StringRes messageRes: Int, duration: Int) {
        Toast.makeText(activity, messageRes, duration).show()
    }

    fun setupToolbar(toolbar: Toolbar) {
        val act = activity
        if (act is AbstractActivity) {
            act.setSupportActionBar(toolbar)
            act.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    @CallSuper
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === android.R.id.home) {
            activity.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showProgressDialog() {

    }

    override fun hideProgressDialog() {

    }

    override fun showSnackbar(message: String, duration: Int) {

    }

    override fun showSnackbar(@StringRes messageRes: Int, duration: Int) {

    }


}