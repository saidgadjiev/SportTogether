package ru.mail.sporttogether.dagger.components

import dagger.Component
import ru.mail.sporttogether.dagger.modules.ManagersModule
import ru.mail.sporttogether.dagger.modules.RestModule
import ru.mail.sporttogether.dagger.modules.RetrofitModule
import ru.mail.sporttogether.dagger.subcomponents.PresenterSubcomponent
import javax.inject.Singleton

/**
 * Created by bagrusss on 30.09.16.
 *
 */
@Singleton
@Component(modules = arrayOf(
        ManagersModule::class,
        RetrofitModule::class,
        RestModule::class))
interface AppMainComponent {
    fun usePresenterComponent(): PresenterSubcomponent
}