package ru.mail.sporttogether.mvp

import android.support.annotation.StringRes
import android.support.v4.app.DialogFragment
import android.widget.Toast

/**
 * Created by bagrusss on 22.01.17
 */
abstract class PresenterDialogFragment<PR : IDialogPresenter> : DialogFragment(), IDialogView {
    protected lateinit var presenter: PR

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

    override fun showToast(message: String, duration: Int) {
        Toast.makeText(context, message, duration).show()
    }

    override fun showToast(@StringRes messageRes: Int, duration: Int) {
        Toast.makeText(context, messageRes, duration).show()
    }

}