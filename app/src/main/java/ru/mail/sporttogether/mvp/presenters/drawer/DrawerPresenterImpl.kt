package ru.mail.sporttogether.mvp.presenters.drawer

import android.content.Context
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.managers.auth.AuthManager
import ru.mail.sporttogether.managers.data.ICredentialsManager
import ru.mail.sporttogether.managers.headers.HeaderManagerImpl
import ru.mail.sporttogether.mvp.views.drawer.IDrawerView
import ru.mail.sporttogether.net.api.AuthorizationAPI
import javax.inject.Inject

/**
 * Created by bagrusss on 15.10.16.
 *
 */
class DrawerPresenterImpl : IDrawerPresenter {

    private var view: IDrawerView? = null
    @Inject lateinit var manager: AuthManager
    @Inject lateinit var api: AuthorizationAPI
    @Inject lateinit var credentialsManager: ICredentialsManager
    @Inject lateinit var context: Context
    @Inject lateinit var headerManager: HeaderManagerImpl


    constructor(view: IDrawerView) {
        App.injector.usePresenterComponent().inject(this)
        this.view = view
    }

    override fun clickSignOut() {
    }

    override fun onDestroy() {
        view = null
    }
}