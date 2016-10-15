package ru.mail.sporttogether.dagger.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by said on 15.10.16.
 */

@Module
@Singleton
class ContextModule {
    private lateinit var context: Context

    constructor(context: Context) {
        this.context = context
    }

    @Provides
    @Singleton
    fun provideContext() = context
}