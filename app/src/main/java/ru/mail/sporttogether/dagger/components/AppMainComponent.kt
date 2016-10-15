package ru.mail.sporttogether.dagger.components

import dagger.Component
<<<<<<< HEAD
import ru.mail.sporttogether.dagger.modules.*
=======
import ru.mail.sporttogether.dagger.modules.AndroidModule
import ru.mail.sporttogether.dagger.modules.ManagersModule
import ru.mail.sporttogether.dagger.modules.RestModule
import ru.mail.sporttogether.dagger.modules.RetrofitModule
>>>>>>> aabb438284c1cc894d5bbc51545486ca9a63723a
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
<<<<<<< HEAD
        ContextModule::class,
        Auth0Module::class))
=======
        AndroidModule::class))
>>>>>>> aabb438284c1cc894d5bbc51545486ca9a63723a
interface AppMainComponent {
    fun usePresenterComponent(): PresenterSubcomponent
}