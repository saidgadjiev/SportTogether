package ru.mail.sporttogether.dagger.subcomponents

import dagger.Subcomponent
import ru.mail.sporttogether.mvp.presenters.auth.LoginActivityPresenterImpl
import ru.mail.sporttogether.mvp.presenters.event.AddEventPresenterImpl
import ru.mail.sporttogether.mvp.presenters.drawer.DrawerPresenterImpl
import ru.mail.sporttogether.mvp.presenters.map.MapPresenterImpl

/**
 * Created by bagrusss on 09.10.16.
 *
 */
@Subcomponent
interface PresenterSubcomponent {
    fun inject(presenter: MapPresenterImpl)
    fun inject(presenter: LoginActivityPresenterImpl)
    fun inject(presenter: AddEventPresenterImpl)
    fun inject(presenter: DrawerPresenterImpl)
}