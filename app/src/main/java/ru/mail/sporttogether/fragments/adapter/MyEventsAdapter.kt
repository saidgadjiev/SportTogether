package ru.mail.sporttogether.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.adapter.holders.MyEventHolder
import ru.mail.sporttogether.net.models.Event

/**
 * Created by Ivan on 20.10.2016
 * edited by bagrusss
 */
class MyEventsAdapter : AbstractEventAdapter<MyEventHolder>(null) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MyEventHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_two_action, parent, false))

    fun doAngry(e: Event) {
        items?.let { items ->
            itemIdToPosition[e.id]?.let {
                items[it] = e
                notifyItemChanged(it)
            }
        }

    }

}