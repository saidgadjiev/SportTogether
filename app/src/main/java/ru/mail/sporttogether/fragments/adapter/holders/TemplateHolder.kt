package ru.mail.sporttogether.fragments.adapter.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import ru.mail.sporttogether.databinding.ItemTwoButtonBinding
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 07.02.17
 */
class TemplateHolder(v: View) : RecyclerView.ViewHolder(v) {

    private val binding: ItemTwoButtonBinding = ItemTwoButtonBinding.bind(v)

    fun onBind(e: Event) {
        
    }
}