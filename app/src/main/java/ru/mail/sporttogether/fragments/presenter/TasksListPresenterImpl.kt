package ru.mail.sporttogether.fragments.presenter

import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.fragments.adapter.views.TasksListView
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.Task
import java.util.*
import javax.inject.Inject

/**
 * Created by bagrusss on 14.02.17
 */
class TasksListPresenterImpl(var view: TasksListView?) : TasksListPresenter {

    @Inject lateinit var eventsApi: EventsAPI

    init {
        App.injector.usePresenterComponent().inject(this)
    }

    private val tasks = LinkedList<Task>()

    override fun addTask(task: String) {
        tasks.add(Task(message = task))
        if (tasks.size == 5) {
            view?.hideFab()
        }
        view?.taskAdded(task)
    }

    override fun removeTask(pos: Int) {
        tasks.removeAt(pos)
        if (tasks.size < 5) {
            view?.showFab()
        }
    }

    override fun createEvent() {

    }

    override fun onDestroy() {
        view = null
        super.onDestroy()
    }

}