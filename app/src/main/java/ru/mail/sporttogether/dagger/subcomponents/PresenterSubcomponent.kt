package ru.mail.sporttogether.dagger.subcomponents

import dagger.Subcomponent
import ru.mail.sporttogether.activities.presenter.*
import ru.mail.sporttogether.fragments.adapter.presenters.TwoActionsHolderPresenter
import ru.mail.sporttogether.fragments.presenter.AbstractEventsListPresenter
import ru.mail.sporttogether.fragments.presenter.EventsMapFragmentPresenterImpl
import ru.mail.sporttogether.fragments.presenter.ResultDialogPresenterImpl

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
    fun inject(presenter: EditEventActivityPresenterImp)

    fun inject(presenter: AbstractEventsListPresenter)

    fun inject(presenter: TwoActionsHolderPresenter)

    fun inject(presenter: ResultDialogPresenterImpl)
}