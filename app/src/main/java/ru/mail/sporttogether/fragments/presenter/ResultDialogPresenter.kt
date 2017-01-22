package ru.mail.sporttogether.fragments.presenter

import ru.mail.sporttogether.mvp.IDialogPresenter
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 22.01.17
 */
interface ResultDialogPresenter : IDialogPresenter {
    fun sendResult(result: String, event: Event)
}