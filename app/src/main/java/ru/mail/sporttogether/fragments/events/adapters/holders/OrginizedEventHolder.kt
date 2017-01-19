package ru.mail.sporttogether.fragments.events.adapters.holders

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import ru.mail.sporttogether.R

/**
 * Created by bagrusss on 18.01.17
 */
class OrginizedEventHolder(v: View) : AbstractTwoActionHolder(v) {

    init {
        data.action1Text.set(itemView.context.getString(R.string.edit).toUpperCase())
        data.action2Text.set(itemView.context.getString(R.string.share).toUpperCase())
    }

    override fun action1Clicked() {

    }

    override fun getAction1Drawable(): Drawable
            = ContextCompat.getDrawable(itemView.context, R.drawable.ic_edit)

    override fun getAction2Drawable(): Drawable
            = ContextCompat.getDrawable(itemView.context, R.drawable.ic_share)


}