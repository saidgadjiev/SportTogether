package ru.mail.sporttogether.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import ru.mail.sporttogether.R
import ru.mail.sporttogether.data.binding.tasks.TaskData
import ru.mail.sporttogether.databinding.ItemTaskBinding
import ru.mail.sporttogether.fragments.CheckingTasks
import ru.mail.sporttogether.net.models.Task
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

class TaskAdapter(
        private val tasks: ArrayList<Task>,
        private val checkingTasks: CheckingTasks,
        val myId: Long,
        val context: Context
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    init {
        tasks.sortBy { item -> !(item.user == null || item.user?.id == myId) }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskAdapter.ViewHolder?, position: Int) {
        val observableChecked = holder?.onBind(tasks[position], myId, context)
        observableChecked!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Task>() {
                    override fun onError(e: Throwable) {
                        Log.e(TAG, e.message)
                    }

                    override fun onNext(task: Task) {
                        Log.d(TAG, "on next checked : " + task.toString())
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
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemTaskBinding = ItemTaskBinding.inflate(inflater, parent, false)
        context
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

        fun onBind(task: Task, myId: Long, context: Context): Observable<Task> {
            data.id.set(task.id.toString())
            data.message.set(task.message)
            val iMayChecked = task.user == null || task.user?.id == myId
            if (task.user == null) {
                data.username.set("")
            } else {
                if (iMayChecked) {
                    data.username.set("задача закрыта вами")
                } else {
                    data.username.set(task.user?.name)
                }
            }
            data.isChecked.set(task.user != null)
            data.iMayChecked.set(iMayChecked) //можно закрыть таск только если он не закрыт или его закрыл я

            if (!iMayChecked) {
                val taskAvatar = binding.taskAvatar
                Glide.with(context).load(task.user?.avatar).placeholder(R.drawable.ic_people).into(taskAvatar)

                //TODO некликабельность чекбокса не всегда срабатывает, если этого не будет, чекбокс всегда будет кликаться
                taskAvatar.setOnClickListener {
                    Log.d(TAG, "clicked")
                }

            }

            val clickEventObservable = Observable.create(Observable.OnSubscribe<Task> { subscriber ->
                binding.taskCheckbox.setOnClickListener(View.OnClickListener { v ->
                    if (subscriber.isUnsubscribed) return@OnClickListener
                    subscriber.onNext(task)
                })
            })

            return clickEventObservable
        }
    }

    companion object {
        val TAG = "#MY " + TaskAdapter::class.java.simpleName
    }

}