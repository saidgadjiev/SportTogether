package ru.mail.sporttogether.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.adapter.holders.EndedEventHolder
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 18.01.17
 */
class EndedEventsAdapter : AbstractEventAdapter<EndedEventHolder>(null) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EndedEventHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_two_action, parent, false)
        return EndedEventHolder(view)
    }

    fun addResultedEvent(e: Event) {
        items?.let {
            it.add(e)
            itemIdToPosition.append(e.id, it.size - 1)
            notifyItemInserted(it.size - 1)
        }
    }


}