package ru.mail.sporttogether.fragments.events.adapters.holders

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.events.adapters.presenters.EndedHolderPresenter

/**
 * Created by bagrusss on 19.01.17
 */
class EndedEventHolder(v: View) : AbstractTwoActionHolder<EndedHolderPresenter>(v) {

    init {
        data.action2Text.set(v.context.getString(R.string.share).toUpperCase())
        /*val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        binding.buttonPanel1.layoutParams = layoutParams*/
    }

    override fun action1Clicked() {

    }

    override fun action2Clicked() {

    }

    override fun getAction1Drawable() = null

    override fun getAction2Drawable(): Drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_share)

}