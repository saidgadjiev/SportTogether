package ru.mail.sporttogether.adapter.events

import android.view.View
import ru.mail.sporttogether.data.binding.myevents.SeparatorData
import ru.mail.sporttogether.databinding.ItemEventSeparetorBinding

/**
 * Created by bagrusss on 10.11.16.
 *
 */
class SeparatorHolder(v: View) : AbstractEventHolder(v) {

    val binding: ItemEventSeparetorBinding
    val data = SeparatorData()

    init {
        binding = ItemEventSeparetorBinding.bind(v)
        binding.data = data
    }

    override fun onBind(ew: EventWrapper) {
        data.text.set(ew.event.name)
    }

}