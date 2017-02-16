package ru.mail.sporttogether.fragments.adapter.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import ru.mail.sporttogether.data.binding.TaskItemData
import ru.mail.sporttogether.databinding.ItemTaskEventBinding
import ru.mail.sporttogether.fragments.adapter.TasksAdapter
import ru.mail.sporttogether.fragments.presenter.TasksListPresenter

/**
 * Created by bagrusss on 14.02.17
 */
class TaskViewHolder(val adapter: TasksAdapter, v: View, val tasksPresenter: TasksListPresenter) :
        RecyclerView.ViewHolder(v), View.OnClickListener {

    private val binding = ItemTaskEventBinding.bind(v)
    private val data = TaskItemData()

    init {
        binding.data = data
        binding.listener = this
    }

    fun onBind(task: String) {
        data.text.set(task)
    }

    override fun onClick(v: View) {
        v.isEnabled = false
        val pos = adapterPosition
        adapter.deleteTask(pos)
        tasksPresenter.removeTask(pos)
        v.isEnabled = true
    }
}