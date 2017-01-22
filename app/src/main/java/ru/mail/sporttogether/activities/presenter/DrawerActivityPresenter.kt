package ru.mail.sporttogether.activities.presenter

import android.os.Bundle
import ru.mail.sporttogether.mvp.IPresenter

/**
 * Created by bagrusss on 15.10.16.
 *
 */
interface DrawerActivityPresenter : IPresenter {
    fun clickSignOut()

    fun logout()

    fun getProfileName(): String

    fun getAvatar(): String

    fun showEventOnMap(bundle: Bundle)
}