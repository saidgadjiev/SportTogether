package ru.mail.sporttogether.dagger.subcomponents

import dagger.Subcomponent
import ru.mail.sporttogether.mvp.presenters.auth.LoginActivityPresenter
import ru.mail.sporttogether.mvp.presenters.map.MapPresenterImpl

/**
 * Created by bagrusss on 09.10.16.
 *
 */
@Subcomponent
interface PresenterSubcomponent {
    fun inject(presenter: MapPresenterImpl)
    fun inject(presenter: LoginActivityPresenter)
}