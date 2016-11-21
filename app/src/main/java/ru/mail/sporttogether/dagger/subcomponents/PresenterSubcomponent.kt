package ru.mail.sporttogether.dagger.subcomponents

import dagger.Subcomponent
import ru.mail.sporttogether.mvp.presenters.drawer.DrawerPresenterImpl
import ru.mail.sporttogether.mvp.presenters.event.AddEventPresenterImpl
import ru.mail.sporttogether.mvp.presenters.event.EventsListPresenterImpl
import ru.mail.sporttogether.mvp.presenters.event.MyEventsPresenterImpl
import ru.mail.sporttogether.mvp.presenters.event.UpdateEventPresenterImpl
import ru.mail.sporttogether.mvp.presenters.map.MapPresenterImpl
import ru.mail.sporttogether.mvp.presenters.splash.SplashActivityPresenterImpl

/**
 * Created by bagrusss on 09.10.16.
 *
 */
@Subcomponent
interface PresenterSubcomponent {
    fun inject(presenter: MapPresenterImpl)
    fun inject(presenter: AddEventPresenterImpl)
    fun inject(presenter: DrawerPresenterImpl)
    fun inject(presenter: MyEventsPresenterImpl)
    fun inject(presenter: EventsListPresenterImpl)
    fun inject(presenter: SplashActivityPresenterImpl)
    fun inject(presenter: UpdateEventPresenterImpl)
}