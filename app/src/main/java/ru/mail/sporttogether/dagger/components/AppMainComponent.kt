package ru.mail.sporttogether.dagger.components

import dagger.Component
import ru.mail.sporttogether.dagger.modules.RestModule
import javax.inject.Singleton

/**
 * Created by bagrusss on 30.09.16.
 *
 */
@Singleton
@Component(modules = arrayOf(RestModule::class))
interface AppMainComponent {

}