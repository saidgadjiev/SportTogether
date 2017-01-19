package ru.mail.sporttogether.fragments.events.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.events.adapters.holders.EndedEventHolder

/**
 * Created by bagrusss on 18.01.17
 */
class EndedEventsAdapter :AbstractEventAdapter<EndedEventHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EndedEventHolder
            = EndedEventHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_two_action, parent, false))


}