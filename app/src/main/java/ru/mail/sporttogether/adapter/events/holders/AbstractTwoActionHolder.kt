package ru.mail.sporttogether.adapter.events.holders

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import ru.mail.sporttogether.data.binding.items.TwoActionsItemData
import ru.mail.sporttogether.data.binding.items.TwoActionsListener
import ru.mail.sporttogether.databinding.ItemTwoActionBinding
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 15.01.17
 */
abstract class AbstractTwoActionHolder(v: View) : RecyclerView.ViewHolder(v), TwoActionsListener {

    protected val binding: ItemTwoActionBinding = ItemTwoActionBinding.bind(v)
    protected val data = TwoActionsItemData()

    init {
        binding.data = data
        data.action1Drawable.set(getAction1Drawable())
        data.action2Drawable.set(getAction2Drawable())
    }

    abstract fun getAction1Drawable(): Drawable
    abstract fun getAction2Drawable(): Drawable

    abstract fun onBind(pos: Int, event: Event)
}