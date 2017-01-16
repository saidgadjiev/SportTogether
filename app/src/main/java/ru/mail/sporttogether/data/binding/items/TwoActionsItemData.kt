package ru.mail.sporttogether.data.binding.items

import android.databinding.ObservableField
import android.graphics.drawable.Drawable


/**
 * Created by bagrusss on 16.01.17
 */
class TwoActionsItemData {
    val eventText = ObservableField<String>()
    val categoryText = ObservableField<String>()
    val distanceText = ObservableField<String>()
    val dateText = ObservableField<String>()

    val action1Text = ObservableField<String>()
    val action2Text = ObservableField<String>()

    val action1Drawable = ObservableField<Drawable>()
    val action2Drawable = ObservableField<Drawable>()

}
