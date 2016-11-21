package ru.mail.sporttogether.dagger.subcomponents

import dagger.Subcomponent
import ru.mail.sporttogether.activities.DrawerActivity

/**
 * Created by said on 15.10.16.
 */
@Subcomponent
interface ViewSubComponent {
    fun inject(view: DrawerActivity)
}