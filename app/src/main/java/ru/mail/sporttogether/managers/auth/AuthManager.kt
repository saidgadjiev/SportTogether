package ru.mail.sporttogether.managers.auth

import android.util.Log
import ru.mail.sporttogether.managers.data.ICredentialsManager
import ru.mail.sporttogether.mvp.views.ISplashView
import ru.mail.sporttogether.mvp.views.drawer.IDrawerView
import ru.mail.sporttogether.net.api.AuthorizationAPI
import ru.mail.sporttogether.net.models.User
import ru.mail.sporttogether.net.responses.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by said on 15.10.16.
 *
 */
class AuthManager(val credentialsManager: ICredentialsManager) {
    fun auth(api: AuthorizationAPI, view: ISplashView?) {
        api.updateAuthorization()
                .subscribeOn(Schedulers.io())
                // жду сервак - одного запроса должно быть достаточно
                .flatMap { any ->
                    api.checkAuthorization()
                }
                .subscribe(object : Subscriber<Response<User>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        Log.e("exception", e.message, e)
                    }

                    override fun onNext(response: Response<User>) {
                        credentialsManager.saveUserData(response.data)
                        view?.startMainActivity()
                    }
                })
    }

    fun unauth(api: AuthorizationAPI, view: IDrawerView?) {
        api.unauthorize()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        Log.e("exception", e.message, e)
                    }

                    override fun onNext(objectResponse: Response<Any>) {
                        view?.startLoginActivity()
                    }
                })
    }
}