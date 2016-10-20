package ru.mail.sporttogether.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.databinding.ItemEventBinding
import ru.mail.sporttogether.net.models.Event
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Ivan on 20.10.2016.
 */
class EventsAdapter : RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private lateinit var events: List<Event>

    constructor(events: List<Event>) {
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
            holder.mName.text = events[position].name
            holder.mDescription.text = events[position].description
            holder.mDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(events[position].date))
            holder.mReports.text = events[position].reports.toString()
            holder.mNowPeople.text = "0"
            holder.mMaxPeople.text = events[position].maxPeople.toString()
        } else {
            Log.e("#MY " + this.javaClass.simpleName, "holder is null")
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemEventBinding = DataBindingUtil.bind(itemView)
        val mName = binding.itemEventName!!
        val mDescription = binding.itemEventDescription!!
        val mDate = binding.itemEventDate!!
        val mReports = binding.itemEventReports!!
        val mMaxPeople = binding.itemEventMaxPeople!!
        val mNowPeople = binding.itemEventNowPeople!!
    }
}