package ru.mail.sporttogether.dagger.subcomponents

import dagger.Subcomponent
import ru.mail.sporttogether.fragments.adapter.holders.SearchItemHolder
import ru.mail.sporttogether.fragments.adapter.presenters.MyEventsHolderPresenter
import ru.mail.sporttogether.fragments.adapter.presenters.TemplatesHolderPresenterImpl

/**
 * Created by said on 15.10.16.
 *
 */
@Subcomponent
interface ViewSubComponent {
    fun inject(view: SearchItemHolder)
    fun inject(view: MyEventsHolderPresenter)
    fun inject(view: TemplatesHolderPresenterImpl)
}