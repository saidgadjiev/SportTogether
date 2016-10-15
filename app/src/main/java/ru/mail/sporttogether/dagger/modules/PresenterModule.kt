package ru.mail.sporttogether.dagger.modules

import dagger.Module
import dagger.Provides
import ru.mail.sporttogether.mvp.presenters.auth.LoginActivityPresenter
import ru.mail.sporttogether.mvp.views.login.ILoginView

/**
 * Created by said on 15.10.16.
 */

@Module
class PresenterModule {
    private lateinit var loginView: ILoginView

    constructor(loginView: ILoginView) {
        this.loginView = loginView
    }

    @Provides
    fun provideLoginActivity() = LoginActivityPresenter(loginView)
}