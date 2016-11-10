package ru.mail.sporttogether.mvp.presenters.auth

import android.content.Context
import android.os.Bundle
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.mvp.views.login.ILoginView
import ru.mail.sporttogether.net.api.AuthorizationAPI
import javax.inject.Inject

/**
 * Created by said on 05.10.16.
 */
class LoginActivityPresenterImpl : ILoginPresenter {
    private var view: ILoginView? = null
    @Inject lateinit var api: AuthorizationAPI
    @Inject lateinit var context: Context

    constructor(view: ILoginView) {
        App.injector.usePresenterComponent().inject(this)
        this.view = view
    }

    fun trySignIn() {

    }

    override fun onCreate(args: Bundle?) {

    }


    override fun onDestroy() {
        view = null
    }
}
