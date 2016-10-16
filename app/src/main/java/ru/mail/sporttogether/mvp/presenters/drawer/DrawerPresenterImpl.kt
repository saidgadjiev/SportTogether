package ru.mail.sporttogether.mvp.presenters.drawer

import android.content.Context
import android.util.Log
import com.auth0.android.facebook.FacebookAuthProvider
import com.facebook.FacebookSdk
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
        if (FacebookSdk.isInitialized()) {
            Log.d("Auth", "Facebook initialized")
        }
        credentialsManager.deleteCredentials(context)
        provider.clearSession()
        manager.unauth(api, view)
    }

    override fun onDestroy() {
        view = null
    }
}