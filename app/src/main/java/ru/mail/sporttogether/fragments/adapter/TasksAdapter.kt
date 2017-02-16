package ru.mail.sporttogether.fragments.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.adapter.holders.TaskViewHolder
import ru.mail.sporttogether.fragments.presenter.TasksListPresenter
import java.util.*

/**
 * Created by bagrusss on 14.02.17
 */
class TasksAdapter(val tasksPresenter: TasksListPresenter) : RecyclerView.Adapter<TaskViewHolder>() {

    private val items = LinkedList<String>()

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_task_event, parent, false)
        return TaskViewHolder(this, view, tasksPresenter)
    }

    fun addTask(task: String) {
        items.add(task)
        notifyItemInserted(items.size)
    }

    fun deleteTask(pos: Int) {
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }

}