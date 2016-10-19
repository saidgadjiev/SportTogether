package ru.mail.sporttogether.mvp.views.login

import com.auth0.android.lock.Lock
import ru.mail.sporttogether.mvp.views.IView

/**
 * Created by said on 15.10.16.
 */
interface ILoginView: IView {
    fun startMainActivity()
    fun startLockActivity(lock: Lock?)
}