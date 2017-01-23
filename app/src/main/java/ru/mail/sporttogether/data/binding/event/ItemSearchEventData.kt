package ru.mail.sporttogether.data.binding.event

import android.databinding.ObservableField

/**
 * Created by bagrusss on 23.01.17
 */
class ItemSearchEventData {
    val category = ObservableField<String>()
    val distance = ObservableField<String>()
    val name = ObservableField<String>()
}