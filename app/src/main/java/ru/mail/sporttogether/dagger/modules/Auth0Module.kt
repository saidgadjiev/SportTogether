package ru.mail.sporttogether.dagger.modules

import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.facebook.FacebookAuthProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by said on 15.10.16.
 */

@Module
@Singleton
class Auth0Module {

    private lateinit var auth0: Auth0

    constructor(auth0: Auth0) {
        this.auth0 = auth0
    }

    @Singleton
    @Provides
    fun provideAuth0() = auth0

    @Singleton
    @Provides
    fun provideAuthClient() = AuthenticationAPIClient(auth0)

    @Singleton
    @Provides
    fun provideFacebookProvider(aClient: AuthenticationAPIClient) = FacebookAuthProvider(aClient)
}