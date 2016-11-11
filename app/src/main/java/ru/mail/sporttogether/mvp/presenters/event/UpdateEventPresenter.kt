package ru.mail.sporttogether.mvp.presenters.event

import ru.mail.sporttogether.mvp.presenters.IPresenter

/**
 * Created by bagrusss on 07.10.16.
 *
 */
interface UpdateEventPresenter : IPresenter {

    fun updateResult(id: Long, result: String)

}