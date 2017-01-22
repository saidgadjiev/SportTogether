package ru.mail.sporttogether.activities.presenter

import android.view.View
import ru.mail.sporttogether.mvp.IPresenter

/**
 * Created by said on 21.11.16.
 */
interface LoginActivityPresenter : IPresenter {
    fun loginClick(view: View?)
    fun logoutFromServer()

}