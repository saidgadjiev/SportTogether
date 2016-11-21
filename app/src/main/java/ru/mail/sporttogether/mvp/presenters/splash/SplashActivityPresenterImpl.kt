package ru.mail.sporttogether.mvp.presenters.splash

import android.content.Context
import android.os.Bundle
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.lock.Lock
import ru.mail.sporttogether.activities.SplashActivity
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.auth.core.socialNetworks.FacebookSocialNetwork
import ru.mail.sporttogether.auth.core.socialNetworks.VKSocialNetwork
import ru.mail.sporttogether.managers.headers.HeaderManagerImpl
import ru.mail.sporttogether.mvp.views.ISplashView
import ru.mail.sporttogether.net.api.AuthorizationAPI
import javax.inject.Inject

/**
 * Created by bagrusss on 07.11.16.
 *
 */
class SplashActivityPresenterImpl : SplashActivityPresenter {
    private var view: ISplashView? = null
    @Inject lateinit var api: AuthorizationAPI
    @Inject lateinit var context: Context
    @Inject lateinit var auth0: Auth0
    @Inject lateinit var headerManager: HeaderManagerImpl
    private lateinit var socialNetworkManager: SocialNetworkManager

    constructor(view: ISplashView) {
        App.injector.usePresenterComponent().inject(this)
        this.view = view
    }

    override fun onCreate(args: Bundle?) {
        socialNetworkManager = SocialNetworkManager.getInstance()

        val networkFacebook = FacebookSocialNetwork(view as SplashActivity)
        val networkVK = VKSocialNetwork(view as SplashActivity)

        socialNetworkManager.addSocialNetwork(networkFacebook)
        socialNetworkManager.addSocialNetwork(networkVK)

        if (tryLogin()) {
            view?.startMainActivity()
        }
        view?.startLoginActivity()
    }

    private fun tryLogin(): Boolean {
        return socialNetworkManager.initializedSocialNetworks.any { it.isConnected }
    }

    override fun onDestroy() {
        view = null
    }
}