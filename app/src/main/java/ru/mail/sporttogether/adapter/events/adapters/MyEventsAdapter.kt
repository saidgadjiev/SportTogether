package ru.mail.sporttogether.adapter.events.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mail.sporttogether.R
import ru.mail.sporttogether.adapter.events.holders.MyEventHolder
import ru.mail.sporttogether.net.models.Event

/**
 * Created by Ivan on 20.10.2016
 * edited by bagrusss
 */
class MyEventsAdapter : AbstractEventAdapter<MyEventHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MyEventHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_two_action, parent, false))


    override fun swap(events: MutableList<Event>) {
        items = events
        notifyDataSetChanged()
    }

}