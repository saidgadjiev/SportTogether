package ru.mail.sporttogether.data.binding

import android.databinding.ObservableField

/**
 * Created by bagrusss on 12.02.17
 */
class EventDetailsData {
    val sport = ObservableField<String>()
    val date = ObservableField<String>()
    val address = ObservableField<String>()
    val odrginizerName = ObservableField<String>()
    val people = ObservableField<String>()
    val description = ObservableField<String>()
}