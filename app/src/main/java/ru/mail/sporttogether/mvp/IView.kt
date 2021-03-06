package ru.mail.sporttogether.mvp

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.widget.Toast
import ru.mail.sporttogether.R

/**
 * Created by bagrusss on 01.10.16.
 *
 */
interface IView {

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT)
    fun showToast(@StringRes messageRes: Int, duration: Int = Toast.LENGTH_SHORT)

    fun showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT)
    fun showSnackbar(@StringRes messageRes: Int, duration: Int = Snackbar.LENGTH_SHORT)

    fun showProgressDialog(@StringRes messageStringRes: Int = R.string.please_wait)
    fun hideProgressDialog()
}