package ru.mail.sporttogether.mvp.presenters.splash

import android.content.Context
import android.os.Bundle
import ru.mail.sporttogether.activities.SplashActivity
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.auth.core.listeners.OnInitializationCompleteListener
import ru.mail.sporttogether.auth.core.social_networks.FacebookSocialNetwork
import ru.mail.sporttogether.auth.core.social_networks.VKSocialNetwork
import ru.mail.sporttogether.managers.headers.HeaderManagerImpl
import ru.mail.sporttogether.mvp.views.ISplashView
import ru.mail.sporttogether.net.api.AuthorizationAPI
import javax.inject.Inject

/**
 * Created by bagrusss on 07.11.16.
 *
 */
class SplashActivityPresenterImpl(view: ISplashView) : SplashActivityPresenter, OnInitializationCompleteListener {

    private var view: ISplashView? = view
    @Inject lateinit var api: AuthorizationAPI
    @Inject lateinit var context: Context
    @Inject lateinit var headerManager: HeaderManagerImpl
    private lateinit var socialNetworkManager: SocialNetworkManager

    override fun onCreate(args: Bundle?) {
        socialNetworkManager = SocialNetworkManager.instance

        val networkFacebook = FacebookSocialNetwork(view as SplashActivity)
        val networkVK = VKSocialNetwork(view as SplashActivity)

        socialNetworkManager.addSocialNetwork(networkFacebook)
        socialNetworkManager.addSocialNetwork(networkVK)
        socialNetworkManager.setOnInitializationCompleteListener(this)

        if (tryLogin()) {
            view?.startMainActivity()

            return
        }
        view?.startLoginActivity()
    }

    private fun tryLogin(): Boolean {
        return socialNetworkManager.initializedSocialNetworks.any { it.isConnected }
    }

    override fun onDestroy() {
        view = null
    }

    override fun onSocialNetworkManagerInitialized() {

    }

    init {
        App.injector.usePresenterComponent().inject(this)
    }
}