package ru.mail.sporttogether.fragments.adapter.holders

import android.view.View
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.data.binding.event.ItemSearchEventData
import ru.mail.sporttogether.databinding.ItemEventSearchBinding
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.net.models.Event
import javax.inject.Inject

/**
 * Created by bagrusss on 23.01.17
 */
class SearchItemHolder(v: View) : AbstractSearchItemHolder(v) {

    val binding: ItemEventSearchBinding = ItemEventSearchBinding.bind(v)
    val data = ItemSearchEventData()
    private lateinit var event: Event

    //TODO view ничего не должна знать о менеджерах, но нет времени
    @Inject lateinit var evenstManager: EventsManager

    init {
        binding.data = data
        binding.listener = this
        App.injector.useViewComponent().inject(this)
    }

    override fun onBind(e: Event) {
        data.category.set(e.category.name)
        data.distance.set(String.format(if (e.distance >= 10f) "%.0f км" else "%.1f км", e.distance))
        data.name.set(e.name)
        event = e
    }

    override fun onItemClicker() {
        evenstManager.showEvent(event)
    }
}