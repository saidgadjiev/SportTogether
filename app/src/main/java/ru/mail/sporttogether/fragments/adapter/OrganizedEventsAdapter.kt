package ru.mail.sporttogether.fragments.adapter

import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.adapter.holders.OrganizedEventHolder
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 18.01.17
 */

class OrganizedEventsAdapter(fm: FragmentManager) : AbstractEventAdapter<OrganizedEventHolder>(fm) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizedEventHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_two_action, parent, false)
        return OrganizedEventHolder(view, fm)
    }

    fun resultEvent(e: Event) {
        itemIdToPosition[e.id]?.let {
            notifyItemChanged(it)
        }
    }


}