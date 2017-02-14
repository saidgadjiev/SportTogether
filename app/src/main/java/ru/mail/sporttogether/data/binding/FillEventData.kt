package ru.mail.sporttogether.data.binding

import android.databinding.ObservableBoolean
import android.databinding.ObservableField

/**
 * Created by bagrusss on 14.02.17
 */
class FillEventData {
    val name = ObservableField<String>()
    val address = ObservableField<String>()
    val date = ObservableField<String>()
    val sport = ObservableField<String>()
    val description = ObservableField<String>()
    val maxPeople = ObservableField<String>()
    val needAddTemplate = ObservableBoolean(true)
    val joinToEvent = ObservableBoolean(true)

}