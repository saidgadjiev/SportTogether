package ru.mail.sporttogether.mvp.presenters.splash

import android.content.Context
import android.os.Bundle
import android.util.Log
import ru.mail.sporttogether.activities.SplashActivity
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.auth.core.SocialNetworkError
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnRequestSocialPersonCompleteListener
import ru.mail.sporttogether.auth.core.persons.SocialPerson
import ru.mail.sporttogether.auth.core.social_networks.FacebookSocialNetwork
import ru.mail.sporttogether.auth.core.social_networks.VKSocialNetwork
import ru.mail.sporttogether.managers.headers.HeaderManagerImpl
import ru.mail.sporttogether.mvp.views.ISplashView
import ru.mail.sporttogether.net.api.AuthorizationAPI
import ru.mail.sporttogether.net.models.Profile
import ru.mail.sporttogether.net.responses.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by bagrusss on 07.11.16.
 *
 */
class SplashActivityPresenterImpl(view: ISplashView) : SplashActivityPresenter, OnRequestSocialPersonCompleteListener, OnLoginCompleteListener {

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
//        socialNetworkManager.setOnInitializationCompleteListener(this)
        socialNetworkManager.checkIsLogged(this)
//        tryLogin()
    }

    private fun tryLogin() {
        for (socialNetwork in socialNetworkManager.initializedSocialNetworks) {
            if (socialNetwork.tryAutoLogin(this)) {
                return
            }
        }
        onError(SocialNetworkError("Not logged in", -1))
    }

    override fun onDestroy() {
        view = null
    }

    override fun onComplete(person: SocialPerson, ID: Int) {
        Log.d("#MY " + javaClass.simpleName, "in on complete : " + person)

        headerManager.clientId = person.id!!
        headerManager.token = socialNetworkManager.getSocialNetwork(ID)!!.token
        api.updateAuthorization(Profile(person.avatarURL!!, person.name!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onCompleted() {

                    }

                    override fun onNext(t: Response<Any>?) {
                        Log.d("#MY " + javaClass.simpleName, "answer from server : " + person)
                        socialNetworkManager.setNetworkID(ID)
                        view?.startMainActivity()
                    }

                    override fun onError(e: Throwable?) {

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

    init {
        App.injector.usePresenterComponent().inject(this)
    }
}