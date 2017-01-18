package ru.mail.sporttogether.fragments.events.adapters.holders

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import ru.mail.sporttogether.R

/**
 * Created by bagrusss on 15.01.17
 */
class MyEventHolder(v: View) : AbstractTwoActionHolder(v) {

    init {
        data.action1Text.set(itemView.context.getString(R.string.angry).toUpperCase())
        data.action2Text.set(itemView.context.getString(R.string.share).toUpperCase())
        binding.rightPart.minimumHeight = v.height
    }

    override fun action1Clicked() {

    }

    override fun action2Clicked() {

    }

    override fun getAction1Drawable(): Drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_report)

    override fun getAction2Drawable(): Drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_share)


}