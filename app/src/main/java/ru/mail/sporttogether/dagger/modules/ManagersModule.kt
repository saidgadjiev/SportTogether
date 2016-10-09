package ru.mail.sporttogether.dagger.modules

import dagger.Module
import dagger.Provides
import ru.mail.sporttogether.managers.HeaderManager
import javax.inject.Singleton

/**
 * Created by bagrusss on 09.10.16.
 *
 */
@Module
@Singleton
class ManagersModule {

    @Singleton
    @Provides
    fun provideHeaderManager(): HeaderManager {
        return HeaderManager()
    }

}