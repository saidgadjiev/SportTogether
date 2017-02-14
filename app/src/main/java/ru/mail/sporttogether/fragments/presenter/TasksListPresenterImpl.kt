package ru.mail.sporttogether.fragments.presenter

import ru.mail.sporttogether.fragments.adapter.views.TasksListView

/**
 * Created by bagrusss on 14.02.17
 */
class TasksListPresenterImpl(var view: TasksListView?) : TasksListPresenter {

    override fun onDestroy() {
        view = null
        super.onDestroy()
    }

}