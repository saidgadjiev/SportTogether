package ru.mail.sporttogether.activities

import android.net.Uri
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.mvp.IPresenter
import ru.mail.sporttogether.mvp.IView
import ru.mail.sporttogether.widgets.ProgressDialogFragment

/**
 * Created by bagrusss on 30.09.16
 */
abstract class PresenterActivity<T : IPresenter> : AppCompatActivity(), IView {

    protected lateinit var presenter: T
    protected val injector = App.injector.useViewComponent()
    private var dialogSpinner: ProgressDialogFragment? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun showToast(message: String, duration: Int) {
        Toast.makeText(this, message, duration).show()
    }

    override fun showToast(@StringRes messageRes: Int, duration: Int) {
        Toast.makeText(this, messageRes, duration).show()

    }

    override fun showProgressDialog(@StringRes messageStringRes: Int) {
        try {
            supportFragmentManager?.let {
                if (dialogSpinner == null) {
                    dialogSpinner = ProgressDialogFragment.newInstance(messageStringRes)
                    dialogSpinner!!.show(it, null)
                }
            }
        } catch (t: Throwable) {

        }
    }

    override fun hideProgressDialog() {
        try {
            dialogSpinner?.let {
                dialogSpinner!!.dismissAllowingStateLoss()
                dialogSpinner = null
            }
        } catch (t: Throwable) {

        }
    }

    override fun showSnackbar(message: String, duration: Int) {

    }

    override fun showSnackbar(@StringRes messageRes: Int, duration: Int) {

    }

    @CallSuper
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun shareToSocial(title: String, description: String) {
        val content = ShareLinkContent
                .Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .setContentTitle(title)
                .setContentDescription(description)
                .build()

        ShareDialog.show(this, content)
    }

    fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        presenter.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        presenter.onRestoreInstanceState(savedInstanceState)
    }


}