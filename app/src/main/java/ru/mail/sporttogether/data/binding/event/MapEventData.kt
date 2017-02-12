package ru.mail.sporttogether.data.binding.event

import android.databinding.ObservableField

/**
 * Created by root on 08.02.17.
 */
class MapEventData {
    val date = ObservableField<String>()
    val category = ObservableField<String>()
    val creatorName = ObservableField<String>()
}