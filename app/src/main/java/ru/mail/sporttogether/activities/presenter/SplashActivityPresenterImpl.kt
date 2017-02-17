package ru.mail.sporttogether.activities.presenter

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import ru.mail.sporttogether.activities.SplashActivity
import ru.mail.sporttogether.activities.view.SplashView
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.auth.core.SocialNetworkError
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.auth.core.SocialPerson
import ru.mail.sporttogether.auth.core.social_networks.FacebookSocialNetwork
import ru.mail.sporttogether.auth.core.social_networks.GoogleSocialNetwork
import ru.mail.sporttogether.auth.core.social_networks.VKSocialNetwork
import ru.mail.sporttogether.managers.HeaderManagerImpl
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.api.AuthorizationAPI
import ru.mail.sporttogether.net.models.Profile
import ru.mail.sporttogether.net.models.User
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by bagrusss on 07.11.16.
 *
 */
class SplashActivityPresenterImpl(view: SplashView) : SplashActivityPresenter {

    private var view: SplashView? = view
    @Inject lateinit var api: AuthorizationAPI
    @Inject lateinit var context: Context
    @Inject lateinit var headerManager: HeaderManagerImpl

    @Inject lateinit var socialNetworkManager: SocialNetworkManager

    init {
        App.injector.usePresenterComponent().inject(this)
    }

    override fun onCreate(args: Bundle?) {
        val networkFacebook = FacebookSocialNetwork(view as SplashActivity)
        val networkVK = VKSocialNetwork(view as SplashActivity)
        val google = GoogleSocialNetwork(view as SplashActivity)

        socialNetworkManager.addSocialNetwork(networkFacebook)
        socialNetworkManager.addSocialNetwork(networkVK)
        socialNetworkManager.addSocialNetwork(google)

        Handler().postDelayed({
            tryLogin()
        }, 1500)
    }

    private fun tryLogin() {
        socialNetworkManager.initializedSocialNetworks
                .filter { it.tryAutoLogin(this) }
                .forEach { return }
        onError(SocialNetworkError("Not logged in", -1))
    }

    override fun onDestroy() {
        view = null
    }

    override fun onComplete(person: SocialPerson, ID: Int) {

        headerManager.clientId = person.id
        headerManager.token = socialNetworkManager.getSocialNetwork(ID)!!.token
        api.updateAuthorization(Profile(person.avatarURL, person.name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<User>>() {
                    override fun onCompleted() {

                    }

                    override fun onNext(resp: Response<User>) {
                        socialNetworkManager.setNetworkID(ID)
                        if (resp.code == 0) {
                            Log.d("#MY " + javaClass.simpleName, "answer from server : " + resp.data)
                            socialNetworkManager.activeUser = resp.data.copy(clientId = "")
                            view?.startMainActivity()
                        } else {
                            Log.e("#MY ", "error when authorize : " + resp.message)
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.e("splash", e.message, e)
                    }
                })
    }

    override fun onSuccess(ID: Int) {
        socialNetworkManager.getSocialNetwork(ID)!!.requestPerson(this)
    }

    override fun onCancel() {
    }

    override fun onError(socialNetworkError: SocialNetworkError) {
        view?.startLoginActivity()
    }
}