package ru.mail.sporttogether.fragments

import ru.mail.sporttogether.fragments.adapter.TasksAdapter
import ru.mail.sporttogether.fragments.adapter.views.TasksListView
import ru.mail.sporttogether.fragments.presenter.TasksListPresenter
import ru.mail.sporttogether.fragments.presenter.TasksListPresenterImpl

/**
 * Created by bagrusss on 14.02.17
 */
class TasksListFragment : AbstractListFragment<TasksListPresenter, TasksAdapter>(), TasksListView {

    override fun getListPresenter() = TasksListPresenterImpl(this)

    override fun getAdapter() = TasksAdapter()

}