package ru.mail.sporttogether.adapter.events.holders

import android.view.View
import ru.mail.sporttogether.adapter.events.EventWrapper
import ru.mail.sporttogether.data.binding.myevents.SeparatorData
import ru.mail.sporttogether.databinding.ItemEventSeparetorBinding

/**
 * Created by bagrusss on 10.11.16.
 *
 */
class SeparatorHolder(v: View) : AbstractEventHolder(v) {

    val binding: ItemEventSeparetorBinding = ItemEventSeparetorBinding.bind(v)
    val data = SeparatorData()

    init {
        binding.data = data
    }

    override fun onBind(ew: EventWrapper) {
        data.text.set(ew.event.name)
    }

}