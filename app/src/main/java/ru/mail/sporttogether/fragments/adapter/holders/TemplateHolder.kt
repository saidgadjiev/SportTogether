package ru.mail.sporttogether.fragments.adapter.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.AddEventActivity
import ru.mail.sporttogether.data.binding.items.TwoButtonItemData
import ru.mail.sporttogether.databinding.ItemTwoButtonBinding
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.utils.DateUtils

/**
 * Created by bagrusss on 07.02.17
 */
class TemplateHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {


    private val binding: ItemTwoButtonBinding = ItemTwoButtonBinding.bind(v)
    private val data = TwoButtonItemData()
    private lateinit var event: Event

    init {
        binding.data = data
        binding.listener = this
        data.button2Text.set(itemView.context.getString(R.string.edit))
        data.button1Text.set(itemView.context.getString(R.string.create_event))
    }

    fun onBind(e: Event) {
        data.nameText.set(e.name)
        data.dateText.set(DateUtils.longToDateTime(e.date))
        data.description.set(e.description)
        event = e
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button1 -> {
                AddEventActivity.start(v.context, event)
            }
            R.id.button2 -> {

            }
        }
    }
}