package ru.mail.sporttogether.adapter.events.adapters

import android.support.v7.widget.RecyclerView
import ru.mail.sporttogether.adapter.events.holders.AbstractTwoActionHolder
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 15.01.17
 */
abstract class AbstractEventAdapter<VH : AbstractTwoActionHolder> : RecyclerView.Adapter<VH>() {
    protected var items: MutableList<Event>? = null

    override fun getItemCount() = items?.size ?: 0

    override fun onBindViewHolder(holder: VH, position: Int) {
        items?.let {
            holder.onBind(position, it[position])
        }
    }

    abstract fun swap(events: MutableList<Event>)

}