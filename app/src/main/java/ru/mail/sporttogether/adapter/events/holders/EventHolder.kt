package ru.mail.sporttogether.adapter.events.holders

import android.view.View
import ru.mail.sporttogether.adapter.events.EventWrapper
import ru.mail.sporttogether.data.binding.event.EventDetailsData
import ru.mail.sporttogether.data.binding.event.ItemClickListener
import ru.mail.sporttogether.databinding.ItemEventBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by bagrusss on 10.11.16.
 *
 */
class EventHolder(v: View, clickListener: ItemClickListener?) : AbstractEventHolder(v) {

    private val binding: ItemEventBinding = ItemEventBinding.bind(v)
    private val data = EventDetailsData()

    init {
        binding.data = data
        binding.listener = clickListener
    }

    override fun onBind(ew: EventWrapper) {
        val event = ew.event
        data.name.set(event.name)
        data.date.set(SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(event.date)))
        data.description.set(event.result ?: event.description)
        data.reports.set(event.reports.toString())
        data.peopleCount.set("" + event.nowPeople + '/' + event.maxPeople)
        data.clickable.set(true)
        binding.event = event
    }

}