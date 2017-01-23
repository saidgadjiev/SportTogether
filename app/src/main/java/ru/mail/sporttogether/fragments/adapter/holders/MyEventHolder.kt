package ru.mail.sporttogether.fragments.adapter.holders

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.adapter.views.MyEventsView
import ru.mail.sporttogether.fragments.presenter.MyEventsHolderPresenter
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 15.01.17
 */
class MyEventHolder(v: View) : AbstractTwoActionHolder<MyEventsHolderPresenter>(v, null), MyEventsView {

    init {
        data.action1Text.set(itemView.context.getString(R.string.angry).toUpperCase())
        data.action2Text.set(itemView.context.getString(R.string.share).toUpperCase())
        presenter = MyEventsHolderPresenter(this)
    }

    override fun action1Clicked() {

    }

    override fun getAction1Drawable(): Drawable
            = ContextCompat.getDrawable(itemView.context, R.drawable.ic_report)

    override fun getAction2Drawable(): Drawable
            = ContextCompat.getDrawable(itemView.context, R.drawable.ic_share)


    override fun onBind(pos: Int, event: Event) {
        super.onBind(pos, event)
        if (event.isReported) {
            data.action1Enabled.set(false)
        }
    }


}