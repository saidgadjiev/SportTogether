package ru.mail.sporttogether.fragments.presenter

import android.util.Log
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.fragments.SettingsFragment
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.api.AuthorizationAPI
import ru.mail.sporttogether.net.models.RemindTime
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by root on 23.01.17.
 */
class SettingsPresenterImpl(var view: SettingsFragment?): SettingsPresenter {
    @Inject lateinit var api: AuthorizationAPI
    @Inject lateinit var socialNetworkManager: SocialNetworkManager

    init {
        App.injector
                .usePresenterComponent()
                .inject(this)
    }

    override fun onResume() {
        super.onResume()
        val reminderTime: Long? = socialNetworkManager.activeUser!!.remindTime
        Log.d("#MY ", "set reminder time : " + reminderTime)
        reminderTime?.let { it ->
            view?.setReminderTime((it / SettingsFragment.MILLS_IN_HOUR).toInt())
        }
    }

    override fun saveReminderTime(time: Long) {
        Log.w("#MY SettingsPresenter", "update time on $time")

        api.updateReminderTime(RemindTime(time))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        Log.e(EventsMapFragmentPresenterImpl.TAG, e.message, e)
                    }

                    override fun onNext(t: Response<Any>) {
                        if (t.code == 0) {
                            view?.showToast("Время уведомления успешно изменено")
                        } else view?.showToast("Время не удалось изменить")

                    }
                })
    }
}