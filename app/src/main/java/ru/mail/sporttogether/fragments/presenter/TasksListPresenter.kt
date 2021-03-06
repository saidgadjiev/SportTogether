package ru.mail.sporttogether.fragments.presenter

import ru.mail.sporttogether.mvp.IPresenter

/**
 * Created by bagrusss on 14.02.17
 */
interface TasksListPresenter : IPresenter {

    fun addTask(task: String)
    fun createEvent()
    fun removeTask(pos: Int)

}