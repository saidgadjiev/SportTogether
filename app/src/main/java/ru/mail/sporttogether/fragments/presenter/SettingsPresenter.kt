package ru.mail.sporttogether.fragments.presenter

import ru.mail.sporttogether.mvp.IPresenter

/**
 * Created by root on 23.01.17.
 */
interface SettingsPresenter: IPresenter {
    fun saveReminderTime(time: Long)
}