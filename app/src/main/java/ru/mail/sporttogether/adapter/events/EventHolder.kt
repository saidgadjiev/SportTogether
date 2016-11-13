package ru.mail.sporttogether.adapter.events

import android.view.View
import ru.mail.sporttogether.data.binding.event.EventDetailsData
import ru.mail.sporttogether.databinding.ItemEventBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by bagrusss on 10.11.16.
 *
 */
class EventHolder(v: View) : AbstractEventHolder(v) {

    private val binding: ItemEventBinding
    private val data = EventDetailsData()

    init {
        binding = ItemEventBinding.bind(v)
        binding.data = data
    }

    override fun onBind(ew: EventWrapper) {
        val event = ew.event
        data.name.set(event.name)
        data.date.set(SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(event.date)))
        data.description.set(event.description)
        data.engryCount.set(event.reports.toString())
        data.peopleCount.set("" + event.nowPeople + '/' + event.maxPeople)
    }

}