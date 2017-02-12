package ru.mail.sporttogether.fragments.adapter.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import ru.mail.sporttogether.mvp.IPresenter

/**
 * Created by bagrusss on 12.02.17
 */
abstract class AbstractPresenterHolder<P : IPresenter>(v: View) : RecyclerView.ViewHolder(v) {
    protected lateinit var presenter: P
}