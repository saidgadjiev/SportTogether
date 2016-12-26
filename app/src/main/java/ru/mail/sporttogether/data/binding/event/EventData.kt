package ru.mail.sporttogether.data.binding.event

import android.databinding.ObservableBoolean
import android.databinding.ObservableField

/**
 * Created by bagrusss on 07.10.16.
 *
 */
class EventData {
    val resultVisibility = ObservableBoolean(false)
    val mainDataVisibility = ObservableBoolean(true)
    val address = ObservableField<String>()
}