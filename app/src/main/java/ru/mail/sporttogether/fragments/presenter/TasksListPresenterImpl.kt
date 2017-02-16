package ru.mail.sporttogether.fragments.presenter

import com.google.firebase.iid.FirebaseInstanceId
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.fragments.adapter.views.TasksListView
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.api.TemplatesApi
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.Task
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Created by bagrusss on 14.02.17
 */
class TasksListPresenterImpl(var view: TasksListView?) : TasksListPresenter {

    @Inject lateinit var eventsApi: EventsAPI
    @Inject lateinit var templatesApi: TemplatesApi
    @Inject lateinit var eventsManager: EventsManager


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
        view?.showProgressDialog()
        val oldEvent = eventsManager.getCreatingEvent()
        oldEvent?.let { oldEvent ->
            oldEvent.tasks = tasks
            addEvent(oldEvent)
            if (oldEvent.id == -1L)
                createTemplate(oldEvent)
        }
    }

    fun addEvent(event: Event) {
        eventsApi.createEvent(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Event>>() {
                    override fun onCompleted() {

                    }

                    override fun onNext(response: Response<Event>) {
                        if (response.code == 0) {
                            eventsManager.addEvent(response.data)
                            val newEvent = response.data
                            eventsManager.setCreatingEvent(newEvent)
                            if (event.isJoined) {
                                ++newEvent.nowPeople
                                join(newEvent.id)
                                newEvent.isJoined = false
                            }
                            eventsManager.showEvent(newEvent)
                            view?.finish()
                        } else {
                            view?.showToast(response.message)
                        }
                        unsubscribe()
                        view?.hideProgressDialog()
                    }

                    override fun onError(e: Throwable) {
                        view?.hideProgressDialog()
                        view?.showToast("Не удалось соединиться с сервером")
                    }

                })
    }


    private fun createTemplate(event: Event) {
        templatesApi.createTemplate(event)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Any>() {
                    override fun onError(e: Throwable) {
                        unsubscribe()
                    }

                    override fun onNext(t: Any) {
                        unsubscribe()
                    }

                    override fun onCompleted() {
                    }

                })
    }

    fun join(id: Long) {
        eventsApi.joinToEvent(id, FirebaseInstanceId.getInstance().token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onNext(t: Response<Any>) {
                        if (t.code != 0)
                            view?.showToast(t.message)
                        unsubscribe()
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        unsubscribe()
                    }

                })
    }

    override fun onDestroy() {
        view = null
        super.onDestroy()
    }

}