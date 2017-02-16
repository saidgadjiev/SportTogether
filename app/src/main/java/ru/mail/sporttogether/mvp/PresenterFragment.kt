package ru.mail.sporttogether.mvp

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast

/**
 * Created by bagrusss on 08.10.16
 */
abstract class PresenterFragment<T : IPresenter> : Fragment(), IView {

    protected lateinit var presenter: T

    override fun showToast(message: String, duration: Int) {
        Toast.makeText(activity, message, duration).show()
    }

    override fun showToast(@StringRes messageRes: Int, duration: Int) {
        Toast.makeText(activity, messageRes, duration).show()
    }

    fun setupToolbar(toolbar: Toolbar, title: String? = null) {
        val act = activity
        if (act is PresenterActivity<*>) {
            act.setSupportActionBar(toolbar)
            act.supportActionBar?.let { toolbar ->
                toolbar.setDisplayHomeAsUpEnabled(true)
                title?.let {
                    toolbar.title = title
                }
            }
        }
    }

    @CallSuper
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showProgressDialog(@StringRes messageStringRes: Int) {
        val activity = activity
        if (activity is PresenterActivity<*>) {
            activity.showProgressDialog()
        }

    }

    override fun hideProgressDialog() {
        val activity = activity
        if (activity is PresenterActivity<*>) {
            activity.hideProgressDialog()
        }
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

    override fun onLowMemory() {
        super.onLowMemory()
        presenter.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

}