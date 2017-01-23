package ru.mail.sporttogether.dagger.subcomponents

import dagger.Subcomponent
import ru.mail.sporttogether.fragments.adapter.holders.SearchItemHolder

/**
 * Created by said on 15.10.16.
 *
 */
@Subcomponent
interface ViewSubComponent {
    fun inject(view: SearchItemHolder)
}