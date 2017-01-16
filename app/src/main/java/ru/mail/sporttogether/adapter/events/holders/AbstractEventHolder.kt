package ru.mail.sporttogether.adapter.events.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import ru.mail.sporttogether.adapter.events.EventWrapper

/**
 * Created by bagrusss on 10.11.16.
 *
 */
abstract class AbstractEventHolder(v: View) : RecyclerView.ViewHolder(v) {
    abstract fun onBind(ew: EventWrapper)
}