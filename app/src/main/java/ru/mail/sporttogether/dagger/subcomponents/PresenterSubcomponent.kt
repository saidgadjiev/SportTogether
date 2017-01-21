package ru.mail.sporttogether.dagger.subcomponents

import dagger.Subcomponent
import ru.mail.sporttogether.fragments.events.adapters.presenters.TwoActionsHolderPresenter
import ru.mail.sporttogether.mvp.presenters.auth.LoginPresenterImpl
import ru.mail.sporttogether.mvp.presenters.drawer.DrawerPresenterImpl
import ru.mail.sporttogether.mvp.presenters.event.AddEventPresenterImpl
import ru.mail.sporttogether.mvp.presenters.event.EventsListPresenterImpl
import ru.mail.sporttogether.mvp.presenters.event.lists.MyEventsPresenterImpl
import ru.mail.sporttogether.mvp.presenters.event.UpdateEventPresenterImpl
import ru.mail.sporttogether.mvp.presenters.event.lists.EndedEventsPresenterImpl
import ru.mail.sporttogether.mvp.presenters.event.lists.AbstractEventsListPresenter
import ru.mail.sporttogether.mvp.presenters.event.lists.OrganizedEventsPresenterImpl
import ru.mail.sporttogether.mvp.presenters.map.MapPresenterImpl
import ru.mail.sporttogether.mvp.presenters.splash.SplashActivityPresenterImpl

/**
 * Created by bagrusss on 09.10.16
 */
@Subcomponent
interface PresenterSubcomponent {
    fun inject(presenter: MapPresenterImpl)
    fun inject(presenter: AddEventPresenterImpl)
    fun inject(presenter: DrawerPresenterImpl)
    fun inject(presenter: EventsListPresenterImpl)
    fun inject(presenter: SplashActivityPresenterImpl)
    fun inject(presenter: UpdateEventPresenterImpl)
    fun inject(presenter: LoginPresenterImpl)

    fun inject(presenter: AbstractEventsListPresenter)

    fun inject(presenter: TwoActionsHolderPresenter)
}