package ru.mail.sporttogether.mvp

import android.support.annotation.StringRes
import android.widget.Toast
import ru.mail.sporttogether.R

/**
 * Created by bagrusss on 22.01.17
 */
interface IDialogView {
    fun showProgressDialog(@StringRes messageStringRes: Int = R.string.please_wait)
    fun hideProgressDialog()

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT)
    fun showToast(@StringRes messageRes: Int, duration: Int = Toast.LENGTH_SHORT)
}