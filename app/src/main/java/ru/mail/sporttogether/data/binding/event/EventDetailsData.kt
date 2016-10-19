package ru.mail.sporttogether.data.binding.event

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.drawable.ColorDrawable

/**
 * Created by bagrusss on 20.10.16.
 *
 */
class EventDetailsData {
    val name = ObservableField<String>()
    val date = ObservableField<String>()
    val isJoined = ObservableBoolean()
    val isAngred = ObservableBoolean()
    val peopleCount = ObservableField<String>()
    val backgroudTextColor = ObservableField<ColorDrawable>()
    val description = ObservableField<ColorDrawable>()
    val isEnded = ObservableBoolean()
}