package ru.mail.sporttogether.data.binding.event

import android.databinding.ObservableField

/**
 * Created by bagrusss on 22.01.17
 */
class EditEventData {
    val name = ObservableField<String>()
    val category = ObservableField<String>()
    val description = ObservableField<String>()
    val datetime = ObservableField<String>()
    val peopleCoint = ObservableField<String>()
}