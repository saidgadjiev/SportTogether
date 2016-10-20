package ru.mail.sporttogether.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.databinding.ItemEventBinding

/**
 * Created by Ivan on 20.10.2016.
 */
class EventsAdapter : RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private lateinit var events: List<String>

    constructor(events: List<String>) {
        this.events = events
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        val binding: ItemEventBinding = ItemEventBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder != null) {
            holder.mText.text = events[position]
        } else {
            Log.e("#MY " + this.javaClass.simpleName, "holder is null")
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemEventBinding = DataBindingUtil.bind(itemView)
        val mText = binding.itemEventText!!
    }
}