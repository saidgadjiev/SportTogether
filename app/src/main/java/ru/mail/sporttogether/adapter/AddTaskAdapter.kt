package ru.mail.sporttogether.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.data.binding.tasks.AddTasksData
import ru.mail.sporttogether.databinding.ItemAddTaskBinding
import ru.mail.sporttogether.net.models.Task
import java.util.*

/**
 * Created by root on 13.01.17.
 */
class AddTaskAdapter(): RecyclerView.Adapter<AddTaskAdapter.ViewHolder>() {
    private val tasks: ArrayList<String> = ArrayList(8)

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun getTasks(): List<Task> {
        return tasks.map { str -> Task(0, str, null, 0) }
    }

    fun addTask(msg: String) {
        tasks.add(msg)
        notifyItemInserted(tasks.size - 1)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.onBind(tasks[position])
        holder?.binding!!.itemAddTaskCloseBtn.setOnClickListener {
            tasks.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        Log.d(TaskAdapter.TAG, "on create viewholder.")
        val inflater = LayoutInflater.from(parent?.context)
        val binding: ItemAddTaskBinding = ItemAddTaskBinding.inflate(inflater, parent, false)
        return AddTaskAdapter.ViewHolder(binding.root)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemAddTaskBinding = DataBindingUtil.bind(itemView)
        val data = AddTasksData()

        init {
            binding.data = data
        }

        fun onBind(message: String) {
            data.message.set(message)
        }
    }
}