package ru.mail.sporttogether.adapter.events

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mail.sporttogether.R
import ru.mail.sporttogether.net.models.Event
import java.util.*

/**
 * Created by Ivan on 20.10.2016.
 *
 */
class MyEventsAdapter : RecyclerView.Adapter<AbstractEventHolder>() {
    private var events: MutableList<Event>? = null

    private var items = LinkedList<EventWrapper>()

    private var organizedLastEvent = 0
    private var endedLastEvent = 0
    private var myLastEvent = 0

    override fun getItemCount() = items.size

    fun clear() {
        items.clear()
    }

    private fun convert(events: MutableList<Event>): List<EventWrapper> =
            events.map {
                EventWrapper(
                        type = if (it.id > 0) TYPE_EVENT else TYPE_SEPARATOR,
                        event = it
                )
            }

    // сначала идут события, которые организовал пользователь
    // потом события, которые закончились,
    // далее события, на которые подписан пользователь

    fun addOrganizedEvents(events: MutableList<Event>) {
        items.addAll(convert(events))
        organizedLastEvent = items.size
    }

    fun addEndedEvents(events: MutableList<Event>) {
        items.addAll(organizedLastEvent, convert(events))
        endedLastEvent = organizedLastEvent + events.size
    }

    fun addMyEvents(events: MutableList<Event>) {
        items.addAll(endedLastEvent, convert(events))
        myLastEvent = endedLastEvent + events.size
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractEventHolder {
        val abstractHolder = if (viewType == 0) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
            EventHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event_separetor, parent, false)
            SeparatorHolder(view)
        }
        return abstractHolder
    }

    override fun onBindViewHolder(holder: AbstractEventHolder, position: Int) {
        holder.onBind(items[position])
    }

    companion object {
        @JvmStatic val TYPE_EVENT = 0
        @JvmStatic val TYPE_SEPARATOR = 1
    }

}