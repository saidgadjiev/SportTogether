package ru.mail.sporttogether.dagger.components

import dagger.Component
import ru.mail.sporttogether.dagger.modules.*
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
        RestModule::class,
        Auth0Module::class,
        AndroidModule::class))
interface AppMainComponent {
    fun usePresenterComponent(): PresenterSubcomponent
}