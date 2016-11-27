package ru.mail.sporttogether.mvp.presenters.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.LoginActivity
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.auth.core.SocialNetworkError
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnRequestSocialPersonCompleteListener
import ru.mail.sporttogether.auth.core.persons.SocialPerson
import ru.mail.sporttogether.managers.headers.HeaderManagerImpl
import ru.mail.sporttogether.mvp.views.login.ILoginView
import ru.mail.sporttogether.net.api.AuthorizationAPI
import ru.mail.sporttogether.net.models.Profile
import ru.mail.sporttogether.net.responses.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by said on 26.11.16.
 */
class LoginPresenterImpl(view: ILoginView) : ILoginPresenter, OnLoginCompleteListener, OnRequestSocialPersonCompleteListener {

    private var view: ILoginView? = view
    private lateinit var socialNetworkManager: SocialNetworkManager
    @Inject lateinit var headerManager: HeaderManagerImpl
    @Inject lateinit var authApi: AuthorizationAPI

    init {
        App.injector.usePresenterComponent().inject(this)
    }

    override fun onCreate(args: Bundle?) {
        socialNetworkManager = SocialNetworkManager.instance
    }

    override fun onResume() {
        socialNetworkManager.onResume()
    }

    override fun onComplete(person: SocialPerson, ID: Int) {
        headerManager.clientId = person.id!!
        headerManager.token = socialNetworkManager.getSocialNetwork(ID)!!.token
        authApi.updateAuthorization(Profile(person.avatarURL!!, person.name!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onCompleted() {

                    }

                    override fun onNext(t: Response<Any>?) {
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
    }

    override fun loginClick(view: View?) {
        when (view?.id) {
            R.id.facebook -> socialNetworkManager.facebookSocialNetwork.login(this.view as LoginActivity, this)
            R.id.vk -> socialNetworkManager.vkSocialNetwork.login(this.view as LoginActivity, this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        socialNetworkManager.onActivityResult(requestCode, resultCode, data)
    }

}