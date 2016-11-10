package ru.mail.sporttogether.adapter.events

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    private var items = LinkedList<EventWrapper>()

    private var organizedLastEvent = 0
    private var myLastEvent = 0
    private var endedLastEvent = 0

    override fun getItemCount() = events?.size ?: 0

    fun swap(events: MutableList<Event>) {
        this.events = events
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
    }

    private fun convert(events: ArrayList<Event>): List<EventWrapper> =
            events.map {
                EventWrapper(
                        type = if (it.id > 0) TYPE_EVENT else TYPE_SEPARATOR,
                        event = it)
            }

    fun addOrginizedEvents(events: ArrayList<Event>) {
        items.addAll(convert(events))
        organizedLastEvent = items.size
    }

    fun addMyEvents(events: ArrayList<Event>) {
        items.addAll(organizedLastEvent, convert(events))
        myLastEvent = organizedLastEvent + events.size
    }

    fun addEndedEvents(events: ArrayList<Event>) {
        items.addAll(myLastEvent, convert(events))
        endedLastEvent = myLastEvent + events.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemEventBinding = ItemEventBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        events?.let {
            holder.name.text = it[position].name
            holder.description.text = it[position].description
            holder.date.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(it[position].date))
            holder.reports.text = it[position].reports.toString()
            holder.nowPeople.text = it[position].nowPeople.toString()
            holder.maxPeople.text = it[position].maxPeople.toString()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemEventBinding = DataBindingUtil.bind(itemView)
        val name: TextView = binding.itemEventName
        val description: TextView = binding.itemEventDescription
        val date: TextView = binding.itemEventDate
        val reports: TextView = binding.itemEventReports
        val maxPeople: TextView = binding.itemEventMaxPeople
        val nowPeople: TextView = binding.itemEventNowPeople
    }

    fun addEvent(event: Event) {
        events?.let {
            it.add(event)
            notifyItemInserted(it.size - 1)
        }

    }

    companion object {
        @JvmStatic val TYPE_EVENT = 0
        @JvmStatic val TYPE_SEPARATOR = 1
    }

}