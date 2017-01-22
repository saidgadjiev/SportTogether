package ru.mail.sporttogether.fragments.adapter.holders

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.adapter.presenters.EndedHolderPresenter
import ru.mail.sporttogether.fragments.adapter.views.EndedEventsView

/**
 * Created by bagrusss on 19.01.17
 */
class EndedEventHolder(v: View) :
        AbstractTwoActionHolder<EndedHolderPresenter>(v, null),
        EndedEventsView {

    init {
        data.action2Text.set(v.context.getString(R.string.share).toUpperCase())
        presenter = EndedHolderPresenter(this)
    }

    override fun action1Clicked() {

    }

    override fun getAction1Drawable() = null

    override fun getAction2Drawable(): Drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_share)

}