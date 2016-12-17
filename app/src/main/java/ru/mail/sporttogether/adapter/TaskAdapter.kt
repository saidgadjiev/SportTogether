package ru.mail.sporttogether.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.data.binding.tasks.TaskData
import ru.mail.sporttogether.databinding.ItemTaskBinding
import ru.mail.sporttogether.fragments.CheckingTasks
import ru.mail.sporttogether.net.models.Task
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

class TaskAdapter(private val tasks: ArrayList<Task>, private val checkingTasks: CheckingTasks, val myId: Long) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskAdapter.ViewHolder?, position: Int) {
        Log.d("#MY " + javaClass.simpleName, "on bind viewholder. position = " + position)
        val observableChecked = holder?.onBind(tasks[position], myId)
        observableChecked!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Task>() {
                    override fun onError(e: Throwable) {
                        Log.e("#MY " + javaClass.simpleName, e.message)
                    }

                    override fun onNext(task: Task) {
                        Log.d("#MY " + javaClass.simpleName, "on next checked : " + task.toString())
                        if (task.user == null) {
                            checkingTasks.checkTask(task)
                        } else {
                            checkingTasks.uncheckTask(task)
                        }

                    }

                    override fun onCompleted() {

                    }

                })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.ViewHolder {
        Log.d("#MY " + javaClass.simpleName, "on create viewholder.")
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemTaskBinding = ItemTaskBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    fun swapTasks() {
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemTaskBinding = DataBindingUtil.bind(itemView)
        val data = TaskData()

        init {
            binding.data = data
        }

        fun onBind(task: Task, myId: Long): Observable<Task> {
            Log.d("#MY " + javaClass.simpleName, "task id : " + task.id!!.toInt())
            Log.d("#MY " + javaClass.simpleName, "task user : " + task.user)
            Log.d("#MY " + javaClass.simpleName, "task user id : " + task.user?.id)
            Log.d("#MY " + javaClass.simpleName, "my id : " + myId)
            data.id.set(Integer.toString(task.id!!.toInt()))
            data.message.set(task.message)
            data.username.set(task.user?.name)
            data.isChecked.set(task.user != null)
            data.iMayChecked.set(task.user == null || task.user.id == myId) //можно закрыть таск только если он не закрыт или его закрыл я
            val clickEventObservable = Observable.create(Observable.OnSubscribe<Task> { subscriber ->
                binding.taskCheckbox.setOnClickListener(View.OnClickListener { v ->
                    if (subscriber.isUnsubscribed) return@OnClickListener
                    subscriber.onNext(task)
                })
            })
            return clickEventObservable
        }
    }

}