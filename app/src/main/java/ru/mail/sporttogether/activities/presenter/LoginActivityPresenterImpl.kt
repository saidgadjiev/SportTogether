package ru.mail.sporttogether.activities.presenter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.LoginActivity
import ru.mail.sporttogether.activities.view.LoginView
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.auth.core.SocialNetworkError
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.auth.core.SocialPerson
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnRequestSocialPersonCompleteListener
import ru.mail.sporttogether.managers.HeaderManagerImpl
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.api.AuthorizationAPI
import ru.mail.sporttogether.net.models.Profile
import ru.mail.sporttogether.net.models.User
import ru.mail.sporttogether.services.UnjoinIntentService
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class LoginActivityPresenterImpl(view: LoginView) : LoginActivityPresenter, OnLoginCompleteListener, OnRequestSocialPersonCompleteListener {

    private var view: LoginView? = view
    @Inject lateinit var socialNetworkManager: SocialNetworkManager
    @Inject lateinit var headerManager: HeaderManagerImpl
    @Inject lateinit var authApi: AuthorizationAPI
    @Inject lateinit var context: Context

    init {
        App.injector.usePresenterComponent().inject(this)
    }

    override fun onResume() {
//        socialNetworkManager.onResume()
    }

    override fun onComplete(person: SocialPerson, ID: Int) {
        headerManager.clientId = person.id
        headerManager.token = socialNetworkManager.getSocialNetwork(ID)!!.token
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit()
                .putString(TOKEN_KEY, headerManager.token)
                .putString(CLIENT_ID_KEY, headerManager.clientId)
                .apply()

        val token = sharedPreferences.getString(TOKEN_KEY, "")
        val clientId = sharedPreferences.getString(CLIENT_ID_KEY, "")
        Log.d(UnjoinIntentService.TAG, "when put token and client id in shared preferences $token, $clientId")

        authApi.updateAuthorization(Profile(person.avatarURL, person.name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<User>>() {
                    override fun onCompleted() {

                    }

                    override fun onNext(resp: Response<User>?) {
                        val user = resp!!.data
                        Log.d("#MY " + javaClass.simpleName, "answer from server : " + user)
                        socialNetworkManager.setNetworkID(ID)
                        socialNetworkManager.activeUser = user
                        view?.startMainActivity()
                    }

                    override fun onError(e: Throwable?) {

                    }
                })
    }

    override fun onSuccess(ID: Int) {
        Log.d("#MY " + javaClass.simpleName, "on success callback")
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

    override fun logoutFromServer() {
        authApi.unauthorize()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onCompleted() {

                    }

                    override fun onNext(resp: Response<Any>?) {
                        Log.d("#MY " + javaClass.simpleName, "unauthorized : " + resp?.message)
                    }

                    override fun onError(e: Throwable?) {

                    }
                })
    }

    companion object {
        val SHARED_PREFERENCES_NAME = "auth_data"
        val TOKEN_KEY = "Token"
        val CLIENT_ID_KEY = "ClientId"
    }

}