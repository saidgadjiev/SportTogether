package ru.mail.sporttogether.adapter.events

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.adapter.holders.SearchItemHolder
import ru.mail.sporttogether.net.models.Event

/**
 * Created by Ivan on 20.10.2016
 */
class EventsAdapter : RecyclerView.Adapter<SearchItemHolder>() {
    private var events: MutableList<Event>? = null

    override fun getItemCount() = events?.size ?: 0

    fun swap(events: MutableList<Event>) {
        this.events = events
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_event_search, parent, false)
        return SearchItemHolder(view)
    }

    override fun onBindViewHolder(holder: SearchItemHolder, position: Int) {
        events?.let {
            holder.onBind(it[position])
        }
    }

}