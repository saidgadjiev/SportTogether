package ru.mail.sporttogether.fragments.events.adapters.holders

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import ru.mail.sporttogether.data.binding.items.TwoActionsItemData
import ru.mail.sporttogether.data.binding.items.TwoActionsListener
import ru.mail.sporttogether.databinding.ItemTwoActionBinding
import ru.mail.sporttogether.fragments.events.adapters.presenters.TwoActionsHolderPresenter
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.utils.DateUtils

/**
 * Created by bagrusss on 15.01.17
 */
abstract class AbstractTwoActionHolder<PR : TwoActionsHolderPresenter>(v: View) :
        RecyclerView.ViewHolder(v),
        TwoActionsListener {

    protected val binding: ItemTwoActionBinding = ItemTwoActionBinding.bind(v)
    protected val data = TwoActionsItemData()
    protected lateinit var presenter: PR
    private lateinit var event: Event

    init {
        binding.data = data
        binding.listener = this
        data.action1Drawable.set(getAction1Drawable())
        data.action2Drawable.set(getAction2Drawable())
    }

    open fun onBind(pos: Int, event: Event) {
        data.eventText.set(event.name)
        data.categoryText.set(event.category.name)
        data.dateText.set(DateUtils.longToDateTime(event.date))
        this.event = event
    }

    abstract fun getAction1Drawable(): Drawable?
    abstract fun getAction2Drawable(): Drawable?

    override fun onBodyClicked() {
        presenter.itemClicked(event)
    }

    override fun action1Clicked() {

    }

    override fun action2Clicked() {

    }


}