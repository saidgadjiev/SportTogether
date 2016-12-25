package ru.mail.sporttogether.mvp.presenters.auth

import android.view.View
import ru.mail.sporttogether.mvp.presenters.IPresenter

/**
 * Created by said on 21.11.16.
 */
interface ILoginPresenter : IPresenter {
    fun loginClick(view: View?)
    fun logoutFromServer()

}