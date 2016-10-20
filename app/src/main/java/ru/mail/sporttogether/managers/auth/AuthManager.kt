package ru.mail.sporttogether.managers.auth

import android.util.Log
import ru.mail.sporttogether.mvp.views.drawer.IDrawerView
import ru.mail.sporttogether.mvp.views.login.ILoginView
import ru.mail.sporttogether.net.api.AuthorizationAPI
import ru.mail.sporttogether.net.responses.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by said on 15.10.16.
 */
class AuthManager {
    fun auth(api: AuthorizationAPI, view: ILoginView?) {
        api.updateAuthorization().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Subscriber<Response<Any>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                Log.e("exception", e.message, e)
            }

            override fun onNext(objectResponse: Response<Any>) {
                view?.startMainActivity()
            }
        })
    }

    fun unauth(api: AuthorizationAPI, view: IDrawerView?) {
        api.unauthorize().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Subscriber<Response<Any>>() {
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