package ru.mail.sporttogether.fragments.adapter.holders

import android.graphics.drawable.Drawable
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.view.View
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.EditEventActivity
import ru.mail.sporttogether.fragments.ResultDialogFragment
import ru.mail.sporttogether.fragments.adapter.presenters.OrganizedEventsHolderPresenter
import ru.mail.sporttogether.fragments.adapter.views.OrganizedEventsView
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 18.01.17
 */
class OrganizedEventHolder(v: View, fm: FragmentManager?) :
        AbstractTwoActionHolder<OrganizedEventsHolderPresenter>(v, fm),
        OrganizedEventsView {

    private var eventStarted = false

    init {
        data.action1Text.set(v.context.getString(R.string.result).toUpperCase())
        data.action2Text.set(v.context.getString(R.string.edit).toUpperCase())
        presenter = OrganizedEventsHolderPresenter(this)
    }

    override fun action1Clicked() {
        fm?.let {
            ResultDialogFragment.show(it, event)
        }
    }

    override fun action2Clicked() {
        if (eventStarted) {
            super.action2Clicked()
        } else {
            EditEventActivity.start(itemView.context, event)
        }
    }

    override fun onBind(pos: Int, event: Event) {
        eventStarted = System.currentTimeMillis() > event.date
        super.onBind(pos, event)
        data.action1Enabled.set(event.result == null && eventStarted)
        if (eventStarted) {
            data.action2Text.set(itemView.context.getString(R.string.share).toUpperCase())
            data.action2Drawable.set(ContextCompat.getDrawable(itemView.context, R.drawable.ic_share))
        } else {
            data.action2Text.set(itemView.context.getString(R.string.edit).toUpperCase())
            data.action2Drawable.set(ContextCompat.getDrawable(itemView.context, R.drawable.ic_edit))
        }
    }

    override fun getAction1Drawable(): Drawable
            = ContextCompat.getDrawable(itemView.context, R.drawable.ic_done)

    override fun getAction2Drawable(): Drawable
            = ContextCompat.getDrawable(itemView.context, R.drawable.ic_edit)


}