package ru.mail.sporttogether.mvp.presenters.drawer

import android.content.Context
import com.auth0.android.facebook.FacebookAuthProvider
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.managers.auth.AuthManager
import ru.mail.sporttogether.managers.data.ICredentialsManager
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
    @Inject lateinit var provider: FacebookAuthProvider

    constructor(view: IDrawerView) {
        App.injector.usePresenterComponent().inject(this)
        this.view = view
    }

    override fun clickSignOut() {
        credentialsManager.deleteCredentials(context)
        manager.unauth(api, view)
    }

    override fun onDestroy() {
        view = null
    }
}