package ru.mail.sporttogether.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.data.binding.event.MapEventData
import ru.mail.sporttogether.databinding.ItemMapEventBinding
import ru.mail.sporttogether.databinding.ItemTaskBinding
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.utils.DateUtils
import java.util.*

/**
 * Created by root on 08.02.17.
 */
class MapEventsAdapter(val events: ArrayList<Event>): RecyclerView.Adapter<MapEventsAdapter.MapEventViewHolder>() {

    fun swap(newEvents: ArrayList<Event>) {
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
        val binding: ItemTaskBinding = ItemTaskBinding.inflate(inflater, parent, false)
        return MapEventViewHolder(binding.root)
    }

    inner class MapEventViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding: ItemMapEventBinding = DataBindingUtil.bind(itemView)

        init {
            binding.data = MapEventData()
        }

        fun onBind(event: Event) {
            binding.mapEventCategory.text = event.category.name
            binding.mapEventDate.text = DateUtils.toXLongDateString(Date(event.date))
        }
    }

    companion object {
        val TAG = "#MY " + MapEventsAdapter::class.java.simpleName.substring(0, 18)
    }
}