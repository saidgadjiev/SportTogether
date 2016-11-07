package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ru.mail.sporttogether.activities.PresenterActivity
import ru.mail.sporttogether.mvp.presenters.IPresenter
import ru.mail.sporttogether.mvp.views.IView

/**
 * Created by bagrusss on 08.10.16.
 *
 */
abstract class PresenterFragment<T : IPresenter> : Fragment(), IView {

    protected lateinit var presenter: T

    override fun showToast(message: String, duration: Int) {
        Toast.makeText(activity, message, duration).show()
    }

    override fun showToast(@StringRes messageRes: Int, duration: Int) {
        Toast.makeText(activity, messageRes, duration).show()
    }

    fun setupToolbar(toolbar: Toolbar) {
        val act = activity
        if (act is PresenterActivity<*>) {
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

    protected open val statusBarHeight: Int
        get() {
            var result = 0

            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId)
            }

            return result
        }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

}