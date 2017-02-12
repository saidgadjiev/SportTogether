package ru.mail.sporttogether.fragments.adapter.holders

import android.graphics.drawable.Drawable
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.EventDetailsActivity
import ru.mail.sporttogether.data.binding.items.TwoActionsItemData
import ru.mail.sporttogether.data.binding.items.TwoActionsListener
import ru.mail.sporttogether.databinding.ItemTwoActionBinding
import ru.mail.sporttogether.fragments.adapter.presenters.TwoActionsHolderPresenter
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.utils.DateUtils
import ru.mail.sporttogether.utils.ShareUtils

/**
 * Created by bagrusss on 15.01.17
 */
abstract class AbstractTwoActionHolder<PR : TwoActionsHolderPresenter>(v: View, protected var fm: FragmentManager?) :
        RecyclerView.ViewHolder(v),
        TwoActionsListener {

    protected val binding: ItemTwoActionBinding = ItemTwoActionBinding.bind(v)
    protected val data = TwoActionsItemData()
    protected lateinit var presenter: PR
    protected lateinit var event: Event

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
        EventDetailsActivity.start(itemView.context, event)
    }

    override fun action1Clicked() {

    }

    override fun action2Clicked() {
        val context = itemView.context
        ShareUtils.createShareIntent(event, context.getString(R.string.share))?.let {
            context.startActivity(it)
        }
    }


}