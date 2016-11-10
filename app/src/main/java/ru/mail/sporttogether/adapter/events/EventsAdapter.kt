package ru.mail.sporttogether.adapter.events

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.data.binding.event.EventDetailsData
import ru.mail.sporttogether.databinding.ItemEventBinding
import ru.mail.sporttogether.net.models.Event
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Ivan on 20.10.2016.
 *
 */
class EventsAdapter : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
    private var events: MutableList<Event>? = null

    override fun getItemCount() = events?.size ?: 0

    fun swap(events: MutableList<Event>) {
        this.events = events
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemEventBinding = ItemEventBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        events?.let {
            holder.onBind(it[position])
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemEventBinding = DataBindingUtil.bind(itemView)
        val data = EventDetailsData()

        init {
            binding.data = data
        }

        fun onBind(e: Event) {
            data.name.set(e.name)
            data.description.set(e.description)
            data.date.set(SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(e.date)))
            data.peopleCount.set("" + e.nowPeople + '/' + e.maxPeople)
            data.engryCount.set(e.reports.toString())
        }
    }

    fun addEvent(event: Event) {
        events?.let {
            it.add(event)
            notifyItemInserted(it.size - 1)
        }
    }

}