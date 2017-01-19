package ru.mail.sporttogether.fragments.events.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.events.adapters.holders.OrganizedEventHolder

/**
 * Created by bagrusss on 18.01.17
 */

class OrginizedEventsAdapter : AbstractEventAdapter<OrganizedEventHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = OrganizedEventHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_two_action, parent, false))


}