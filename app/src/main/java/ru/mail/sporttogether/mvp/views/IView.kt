package ru.mail.sporttogether.mvp.views

import android.widget.Toast

/**
 * Created by bagrusss on 01.10.16.
 *
 */
interface IView {
    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT)

    fun showProgressDialog()

    fun hideProgressDialog()

}