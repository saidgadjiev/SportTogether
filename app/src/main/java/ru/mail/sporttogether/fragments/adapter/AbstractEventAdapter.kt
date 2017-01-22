package ru.mail.sporttogether.fragments.adapter

import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.util.LongSparseArray
import ru.mail.sporttogether.fragments.adapter.holders.AbstractTwoActionHolder
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 15.01.17
 */
abstract class AbstractEventAdapter<VH : AbstractTwoActionHolder<*>>(protected val fm: FragmentManager?) :
        RecyclerView.Adapter<VH>() {

    protected var items: MutableList<Event>? = null
    protected var itemIdToPosition = LongSparseArray<Int>()

    override fun getItemCount() = items?.size ?: 0

    override fun onBindViewHolder(holder: VH, position: Int) {
        items?.let {
            holder.onBind(position, it[position])
        }
    }

    fun swap(events: MutableList<Event>) {
        items = events
        updatePositions()
        notifyDataSetChanged()
    }

    protected fun updatePositions() {
        itemIdToPosition.clear()
        items?.let {
            for (i in 0..it.size - 1) {
                itemIdToPosition.put(it[i].id, i)
            }
        }

    }

}