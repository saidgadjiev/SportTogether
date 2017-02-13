package ru.mail.sporttogether.fragments.adapter.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import ru.mail.sporttogether.data.binding.event.SearchItemClickListener
import ru.mail.sporttogether.net.models.Event

/**
 * Created by root on 13.02.17.
 */
abstract class AbstractSearchItemHolder(v: View) : RecyclerView.ViewHolder(v), SearchItemClickListener {

     abstract fun onBind(e: Event)

    companion object {
        val TAG = AbstractSearchItemHolder::class.java.simpleName
    }
}