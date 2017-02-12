package ru.mail.sporttogether.data.binding

import android.databinding.ObservableBoolean
import android.databinding.ObservableField

/**
 * Created by bagrusss on 12.02.17
 */
class EventDetailData {
    val sport = ObservableField<String>()
    val date = ObservableField<String>()
    val address = ObservableField<String>()
    val organizerName = ObservableField<String>()
    val people = ObservableField<String>()
    val description = ObservableField<String>()
    val isEnded = ObservableBoolean(false)
}