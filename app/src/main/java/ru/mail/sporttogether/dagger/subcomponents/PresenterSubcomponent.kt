package ru.mail.sporttogether.dagger.subcomponents

import dagger.Subcomponent
import ru.mail.sporttogether.fragments.adapter.presenters.TwoActionsHolderPresenter
import ru.mail.sporttogether.activities.presenter.LoginActivityPresenterImpl
import ru.mail.sporttogether.activities.presenter.DrawerActivityPresenterImpl
import ru.mail.sporttogether.activities.presenter.AddEventPresenterImpl
import ru.mail.sporttogether.fragments.adapter.presenters.MyEventsPresenterImpl
import ru.mail.sporttogether.fragments.adapter.presenters.EndedEventsPresenterImpl
import ru.mail.sporttogether.fragments.adapter.presenters.AbstractEventsListPresenter
import ru.mail.sporttogether.fragments.adapter.presenters.OrganizedEventsPresenterImpl
import ru.mail.sporttogether.fragments.presenter.EventsMapFragmentPresenterImpl
import ru.mail.sporttogether.activities.presenter.SplashActivityPresenterImpl

/**
 * Created by bagrusss on 09.10.16
 */
@Subcomponent
interface PresenterSubcomponent {
    fun inject(presenter: EventsMapFragmentPresenterImpl)
    fun inject(presenter: AddEventPresenterImpl)
    fun inject(presenter: DrawerActivityPresenterImpl)
    fun inject(presenter: SplashActivityPresenterImpl)
    fun inject(presenter: LoginActivityPresenterImpl)

    fun inject(presenter: AbstractEventsListPresenter)

    fun inject(presenter: TwoActionsHolderPresenter)
}