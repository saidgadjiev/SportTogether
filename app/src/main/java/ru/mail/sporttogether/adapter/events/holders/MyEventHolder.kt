package ru.mail.sporttogether.adapter.events.holders

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import ru.mail.sporttogether.R
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.utils.DateUtils

/**
 * Created by bagrusss on 15.01.17
 */
class MyEventHolder(v: View) : AbstractTwoActionHolder(v) {

    init {
        data.action1Text.set(itemView.context.getString(R.string.angry).toUpperCase())
        data.action2Text.set(itemView.context.getString(R.string.share).toUpperCase())
    }

    override fun action1Clicked() {

    }

    override fun action2Clicked() {

    }

    override fun getAction1Drawable(): Drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_report)

    override fun getAction2Drawable(): Drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_share)

    override fun onBind(pos: Int, event: Event) {
        data.eventText.set(event.name)
        data.categoryText.set(event.category.name)
        data.dateText.set(DateUtils.longToDateTime(event.date))
    }

}