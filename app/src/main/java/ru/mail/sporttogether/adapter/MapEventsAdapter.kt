package ru.mail.sporttogether.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.data.binding.event.MapEventData
import ru.mail.sporttogether.databinding.ItemMapEventBinding
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.utils.DateUtils
import java.util.*

/**
 * Created by root on 08.02.17.
 */
class MapEventsAdapter(val events: MutableList<Event>): RecyclerView.Adapter<MapEventsAdapter.MapEventViewHolder>() {

    fun swap(newEvents: MutableList<Event>) {
        events.clear()
        events.addAll(newEvents)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MapEventViewHolder?, position: Int) {
        holder?.onBind(events[position])
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapEventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemMapEventBinding = ItemMapEventBinding.inflate(inflater, parent, false)
        return MapEventViewHolder(binding.root)
    }

    inner class MapEventViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding: ItemMapEventBinding = DataBindingUtil.bind(itemView)
        val data = MapEventData()

        init {
            binding.data = data
        }

        fun onBind(event: Event) {
            data.category.set(event.category.name)
            data.date.set(DateUtils.toDateWithoutYearString(Date(event.date)))
            data.creatorName.set(event.user.name)
        }
    }

    companion object {
        val TAG = "#MY " + MapEventsAdapter::class.java.simpleName
    }
}