package ru.mail.sporttogether.activities.presenter

import android.content.Context
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.managers.HeaderManagerImpl
import ru.mail.sporttogether.activities.view.DrawerView
import ru.mail.sporttogether.net.api.AuthorizationAPI
import javax.inject.Inject

/**
 * Created by bagrusss on 15.10.16.
 *
 */
class DrawerActivityPresenterImpl(view: DrawerView) : DrawerActivityPresenter {

    private var view: DrawerView? = view
    @Inject lateinit var api: AuthorizationAPI
    @Inject lateinit var context: Context
    @Inject lateinit var headerManager: HeaderManagerImpl


    override fun clickSignOut() {
    }

    override fun onDestroy() {
        view = null
    }

    init {
        App.injector.usePresenterComponent().inject(this)
    }
}