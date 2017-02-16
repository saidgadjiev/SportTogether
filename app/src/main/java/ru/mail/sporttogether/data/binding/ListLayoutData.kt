package ru.mail.sporttogether.data.binding

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.drawable.Drawable

/**
 * Created by bagrusss on 05.02.17
 */
class ListLayoutData {
    val isEmpty = ObservableBoolean(false)
    val emptyText = ObservableField<String>()
    val emptyDrawable = ObservableField<Drawable>()
    val isFabVisible = ObservableBoolean(false)
}