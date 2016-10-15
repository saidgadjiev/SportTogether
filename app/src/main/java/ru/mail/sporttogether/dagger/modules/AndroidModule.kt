package ru.mail.sporttogether.dagger.modules

import dagger.Module
import ru.mail.sporttogether.app.App

/**
 * Created by bagrusss on 15.10.16.
 *
 */
@Module
class AndroidModule {
    fun provideContext() = App.context
}