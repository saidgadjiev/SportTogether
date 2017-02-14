package ru.mail.sporttogether.fragments.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ru.mail.sporttogether.fragments.adapter.holders.TaskViewHolder
import java.util.*

/**
 * Created by bagrusss on 14.02.17
 */
class TasksAdapter : RecyclerView.Adapter<TaskViewHolder>() {

    private val items = LinkedList<String>()

    override fun onBindViewHolder(holder: TaskViewHolder?, position: Int) {

    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TaskViewHolder? {
        return null
    }

}