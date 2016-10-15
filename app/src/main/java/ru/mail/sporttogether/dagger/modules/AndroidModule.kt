package ru.mail.sporttogether.dagger.modules

import dagger.Module
import dagger.Provides
import ru.mail.sporttogether.app.App

/**
 * Created by bagrusss on 15.10.16.
 *
 */
@Module
class AndroidModule {

    @Provides
    fun provideContext() = App.context
}