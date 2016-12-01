package ru.mail.sporttogether.dagger.subcomponents

import dagger.Subcomponent
import ru.mail.sporttogether.activities.DrawerActivity
import ru.mail.sporttogether.fragments.events.EventsMapFragment

/**
 * Created by said on 15.10.16.
 */
@Subcomponent
interface ViewSubComponent {
    fun inject(view: DrawerActivity)
    fun inject(view: EventsMapFragment)
}