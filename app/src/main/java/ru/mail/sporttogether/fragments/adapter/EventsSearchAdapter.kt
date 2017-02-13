package ru.mail.sporttogether.fragments.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.adapter.holders.AbstractSearchItemHolder
import ru.mail.sporttogether.fragments.adapter.holders.SearchItemHolder
import ru.mail.sporttogether.fragments.adapter.holders.SetDateHolder
import ru.mail.sporttogether.net.models.Event

/**
 * Created by Ivan on 20.10.2016
 */
class EventsSearchAdapter : RecyclerView.Adapter<AbstractSearchItemHolder>() {
    private var events: MutableList<Event>? = null

    override fun getItemCount() = events?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) SET_DATE_TYPE else SEARCH_ITEM_TYPE
    }

    fun swap(events: MutableList<Event>) {
        this.events = events
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractSearchItemHolder {
        var holder: AbstractSearchItemHolder? = null
        when(viewType) {
            SET_DATE_TYPE -> {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_event_search_set_date, parent, false)
                holder = SetDateHolder(view)
            }
            else -> {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_event_search, parent, false)
                holder = SearchItemHolder(view)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: AbstractSearchItemHolder, position: Int) {
        when (position) {
            0 -> {}
            else -> {
                events?.let {
                    holder.onBind(it[position - 1])
                }
            }
        }

    }

    companion object {
        private val SET_DATE_TYPE = 0
        private val SEARCH_ITEM_TYPE = 1
    }
}