package ru.mail.sporttogether.fragments.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.adapter.holders.TemplateHolder
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 07.02.17
 */
class TemplatesAdapter : RecyclerView.Adapter<TemplateHolder>() {
    private var items: MutableList<Event>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TemplateHolder(inflater.inflate(R.layout.item_two_button, parent, false))
    }

    override fun onBindViewHolder(holder: TemplateHolder, position: Int) {
        items?.let {
            holder.onBind(it[position])
        }
    }

    override fun getItemCount() = items?.size ?: 0

}