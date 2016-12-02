package ru.mail.sporttogether.dagger.components

import dagger.Component
import ru.mail.sporttogether.dagger.modules.AndroidModule
import ru.mail.sporttogether.dagger.modules.ManagersModule
import ru.mail.sporttogether.dagger.modules.RestModule
import ru.mail.sporttogether.dagger.modules.RetrofitModule
import ru.mail.sporttogether.dagger.subcomponents.PresenterSubcomponent
import ru.mail.sporttogether.dagger.subcomponents.ServiceComponent
import ru.mail.sporttogether.dagger.subcomponents.SharedSubComponent
import ru.mail.sporttogether.dagger.subcomponents.ViewSubComponent
import javax.inject.Singleton

/**
 * Created by bagrusss on 30.09.16.
 *
 */
@Singleton
@Component(modules = arrayOf(
        ManagersModule::class,
        RetrofitModule::class,
        RestModule::class,
        AndroidModule::class))
interface AppMainComponent {
    fun usePresenterComponent(): PresenterSubcomponent
    fun useServiceComponent(): ServiceComponent
    fun useViewComponent(): ViewSubComponent
    fun useSharedComponent(): SharedSubComponent
}