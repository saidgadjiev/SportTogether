package ru.mail.sporttogether.fragments.adapter.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import ru.mail.sporttogether.data.binding.event.ItemSearchEventData
import ru.mail.sporttogether.data.binding.event.SearchItemClickListener
import ru.mail.sporttogether.databinding.ItemEventSearchBinding
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 23.01.17
 */
class SearchItemHolder(v: View) : RecyclerView.ViewHolder(v), SearchItemClickListener {

    val binding: ItemEventSearchBinding = ItemEventSearchBinding.bind(v)
    val data = ItemSearchEventData()

    init {
        binding.data = data
        binding.listener = this
    }

    fun onBind(e: Event) {
        data.category.set(e.category.name)
        data.distance.set(String.format(if (e.distance >= 10f) "%.0f км" else "%.1f км", e.distance))
        data.name.set(e.name)
    }

    override fun onItemClicker() {

    }
}